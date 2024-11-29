package de.axa.robin.vertragsverwaltung.user_interaction;

import de.axa.robin.vertragsverwaltung.modell.*;
import de.axa.robin.vertragsverwaltung.config.Setup;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.storage.editor.Create;
import de.axa.robin.vertragsverwaltung.storage.editor.Edit;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.time.LocalDate;

@Controller
public class MenuSpring {
    private int vsnr;
    private final Setup setup = new Setup();
    private final Scanner scanner = new Scanner(System.in);
    private final Input input = new Input(scanner);
    private final Output output = new Output();
    private final Vertragsverwaltung vertragsverwaltung = new Vertragsverwaltung(setup);
    private final Edit edit = new Edit(input, vertragsverwaltung, output);
    private final Create create = new Create(input, vertragsverwaltung, output);
    private List<Vertrag> vertrage = vertragsverwaltung.getVertrage();

    public int getVsnr() {
        return vsnr;
    }

    @GetMapping("/printVertrage")
    public String showAll(Model model) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.GERMANY));
        vertrage = vertragsverwaltung.getVertrage();
        BigDecimal summe = BigDecimal.ZERO;
        for (Vertrag v : vertrage) {
            summe = summe.add(BigDecimal.valueOf(v.getPreis() * (v.getMonatlich() ? 12 : 1)));
            v.setFormattedPreis(decimalFormat.format(v.getPreis()));
        }
        model.addAttribute("vertrage", vertrage);
        model.addAttribute("preis", summe);
        return "printVertrage";
    }

    @GetMapping("/")
    public String startWebsite() {
        return "index";
    }

    @GetMapping("/showDelete")
    public String showDelete(Model model) {
        model.addAttribute("showFields", true);
        return "handleVertrag";
    }

    @GetMapping("/editPreis")
    public String editPreis(Model model) {
        model.addAttribute("preisdto", new PreisDTO());
        return "editPreis";
    }

    @PostMapping("/precalcPreis")
    @ResponseBody
    public Map<String, Object> editPreis(@ModelAttribute PreisDTO preisDTO) {
        double factor = 1.5, factoralter = 0.1, factorspeed = 0.4;
        try (JsonReader reader = Json.createReader(new FileReader(setup.getPreisPath()))) {
            JsonObject jsonObject = reader.readObject();
            factor = jsonObject.getJsonNumber("factor").doubleValue();
            factoralter = jsonObject.getJsonNumber("factorage").doubleValue();
            factorspeed = jsonObject.getJsonNumber("factorspeed").doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigDecimal preis = edit.recalcpricerun(preisDTO.getFaktor(), preisDTO.getAge(), preisDTO.getSpeed(), vertrage);
        Map<String, Object> response = new HashMap<>();
        response.put("preis", preis.setScale(2, RoundingMode.HALF_DOWN).toString().replace('.', ',') + " €");
        edit.recalcpricerun(factor, factoralter, factorspeed, vertrage);
        return response;
    }

    @PostMapping("/editPreis")
    public String editPreis(@ModelAttribute PreisDTO preisDTO, Model model) {
        BigDecimal preis = edit.recalcpricerun(preisDTO.getFaktor(), preisDTO.getAge(), preisDTO.getSpeed(), vertrage);
        model.addAttribute("confirm", "Preise erfolgreich aktualisiert! neue Preissumme: " + preis.setScale(2, RoundingMode.HALF_DOWN).toString().replace('.', ',') + "€ pro Jahr");
        return "index";
    }

    @GetMapping("/createVertrag")
    public String createVertrag(Model model) {
        vsnr = create.createvsnr();
        VertragDTO vertragDTO = new VertragDTO();
        vertragDTO.setGender("M");
        vertragDTO.setAbrechnung("true");
        model.addAttribute("vertragdto", vertragDTO);
        model.addAttribute("vsnr", vsnr);
        return "createVertrag";
    }

    @PostMapping("/createPreis")
    @ResponseBody
    public Map<String, Object> createPreis(@ModelAttribute VertragDTO vertragdto) {
        boolean monatlich = Objects.equals(vertragdto.getAbrechnung(), "true");
        Map<String, Object> response = new HashMap<>();
        if (!vertragdto.getPlz().isEmpty()) {
            int PLZ = Integer.parseInt(vertragdto.getPlz());
            Partner partner = new Partner(vertragdto.getVorname(), vertragdto.getNachname(), vertragdto.getGender().charAt(0), vertragdto.getBirth(), vertragdto.getLand(), vertragdto.getStrasse(), vertragdto.getHausnummer(), PLZ, vertragdto.getStadt(), vertragdto.getBundesland());
            Fahrzeug fahrzeug = new Fahrzeug(vertragdto.getKennzeichen(), vertragdto.getHersteller(), vertragdto.getTyp(), vertragdto.getSpeed(), vertragdto.getWkz());
            double preis = create.createPreis(monatlich, partner, fahrzeug);
            response.put("preis", String.format(Locale.GERMANY, "%.2f €", preis));
        } else {
            response.put("preis", "--,-- €");
        }
        return response;
    }

    @PostMapping("/showEdit")
    public String editVertrag(@ModelAttribute VertragDTO vertragdto, Model model) {
        vertragsverwaltung.vertragLoeschen(vsnr);
        boolean monatlich = Objects.equals(vertragdto.getAbrechnung(), "true");
        int plzint = Integer.parseInt(vertragdto.getPlz());
        Partner partner = new Partner(vertragdto.getVorname(), vertragdto.getNachname(), vertragdto.getGender().charAt(0), vertragdto.getBirth(), vertragdto.getLand(), vertragdto.getStrasse(), vertragdto.getHausnummer(), plzint, vertragdto.getStadt(), vertragdto.getBundesland());
        Fahrzeug fahrzeug = new Fahrzeug(vertragdto.getKennzeichen(), vertragdto.getHersteller(), vertragdto.getTyp(), vertragdto.getSpeed(), vertragdto.getWkz());
        double preis = create.createPreis(monatlich, partner, fahrzeug);
        Vertrag vertrag = new Vertrag(vsnr, monatlich, preis, vertragdto.getStart(), vertragdto.getEnd(), vertragdto.getCreate(), fahrzeug, partner);
        vertragsverwaltung.vertragAnlegen(vertrag);
        model.addAttribute("confirm", "Vertrag mit VSNR " + vsnr + " erfolgreich bearbeitet! Neuer Preis: " + String.valueOf(preis).replace('.', ',') + "€");
        return "index";
    }

    @PostMapping("/createVertrag")
    public String createVertrag(@ModelAttribute VertragDTO vertragdto, Model model) {
        boolean monatlich = Objects.equals(vertragdto.getAbrechnung(), "true");
        int plzint = Integer.parseInt(vertragdto.getPlz());
        int vsnr = create.createvsnr();
        Partner partner = new Partner(vertragdto.getVorname(), vertragdto.getNachname(), vertragdto.getGender().charAt(0), vertragdto.getBirth(), vertragdto.getLand(), vertragdto.getStrasse(), vertragdto.getHausnummer(), plzint, vertragdto.getStadt(), vertragdto.getBundesland());
        Fahrzeug fahrzeug = new Fahrzeug(vertragdto.getKennzeichen(), vertragdto.getHersteller(), vertragdto.getTyp(), vertragdto.getSpeed(), vertragdto.getWkz());
        double preis = create.createPreis(monatlich, partner, fahrzeug);
        Vertrag vertrag = new Vertrag(vsnr, monatlich, preis, vertragdto.getStart(), vertragdto.getEnd(), vertragdto.getCreate(), fahrzeug, partner);
        vertragsverwaltung.vertragAnlegen(vertrag);
        model.addAttribute("confirm", "Vertrag mit VSNR " + vsnr + " erfolgreich erstellt! Preis: " + String.valueOf(preis).replace('.', ',') + "€");
        return "index";
    }

    @PostMapping("/showDelete")
    public String deleteVertrag(Model model) {
        vertragsverwaltung.vertragLoeschen(vsnr);
        model.addAttribute("confirm", "Vertrag erfolgreich gelöscht!");
        return "index";
    }

    @PostMapping("/")
    public String processPrintVertrag(@RequestParam int VSNR, Model model) {
        vsnr = VSNR;
        Vertrag v = vertragsverwaltung.getVertrag(VSNR);
        if (v == null) {
            model.addAttribute("result", "Vertrag nicht gefunden!");
            return "index";
        }
        VertragDTO vertragDTO = new VertragDTO();
        model.addAttribute("vertragdto", vertragDTO);
        model.addAttribute("vsnr", VSNR);
        model.addAttribute("preis", String.valueOf(v.getPreis()).replace('.', ','));
        model.addAttribute("preisnew", String.valueOf(v.getPreis()).replace('.', ','));
        model.addAttribute("abrechnungszeitraumMonatlich", v.getMonatlich());
        vertragDTO.setAbrechnung(v.getMonatlich() ? "true" : "false");
        model.addAttribute("start", v.getVersicherungsbeginn());
        model.addAttribute("end", v.getVersicherungsablauf());
        model.addAttribute("create", v.getAntragsDatum());
        model.addAttribute("kennzeichen", v.getFahrzeug().getAmtlichesKennzeichen());
        vertragDTO.setKennzeichen(v.getFahrzeug().getAmtlichesKennzeichen());
        model.addAttribute("hersteller", v.getFahrzeug().getHersteller());
        vertragDTO.setHersteller(v.getFahrzeug().getHersteller());
        model.addAttribute("typ", v.getFahrzeug().getTyp());
        vertragDTO.setTyp(v.getFahrzeug().getTyp());
        model.addAttribute("maxspeed", v.getFahrzeug().getHoechstgeschwindigkeit());
        vertragDTO.setSpeed(v.getFahrzeug().getHoechstgeschwindigkeit());
        model.addAttribute("wkz", v.getFahrzeug().getWagnisskennziffer());
        vertragDTO.setWkz(v.getFahrzeug().getWagnisskennziffer());
        model.addAttribute("vorname", v.getPartner().getVorname());
        vertragDTO.setVorname(v.getPartner().getVorname());
        model.addAttribute("nachname", v.getPartner().getNachname());
        vertragDTO.setNachname(v.getPartner().getNachname());
        model.addAttribute("geschlecht", v.getPartner().getGeschlecht());
        vertragDTO.setGender(String.valueOf(v.getPartner().getGeschlecht()));
        model.addAttribute("birth", v.getPartner().getGeburtsdatum());
        model.addAttribute("strasse", v.getPartner().getStrasse());
        vertragDTO.setStrasse(v.getPartner().getStrasse());
        model.addAttribute("hausnummer", v.getPartner().getHausnummer());
        vertragDTO.setHausnummer(v.getPartner().getHausnummer());
        model.addAttribute("plz", v.getPartner().getPlz());
        vertragDTO.setPlz(String.valueOf(v.getPartner().getPlz()));
        model.addAttribute("stadt", v.getPartner().getStadt());
        vertragDTO.setStadt(v.getPartner().getStadt());
        model.addAttribute("bundesland", v.getPartner().getBundesland());
        vertragDTO.setBundesland(v.getPartner().getBundesland());
        model.addAttribute("land", v.getPartner().getLand());
        vertragDTO.setLand(v.getPartner().getLand());
        if (v.getVersicherungsablauf().isBefore(LocalDate.now())) {
            model.addAttribute("gueltig", "Vertrag abgelaufen!");
        }
        return "handleVertrag";
    }

    @GetMapping("/showVertrag")
    public String showWelcomePage() {
        return "handleVertrag";
    }
}