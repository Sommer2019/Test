package de.axa.robin.vertragsverwaltung.user_interaction;

import de.axa.robin.vertragsverwaltung.modell.*;
import de.axa.robin.vertragsverwaltung.storage.Setup;
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
import java.util.*;


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

    private final Create creator = new Create(input, vertragsverwaltung, output);

    @GetMapping("/printVertrage")
    public String showAll(Model model) {
        vertrage = vertragsverwaltung.getVertrage();
        BigDecimal summe = BigDecimal.ZERO;
        for (Vertrag v : vertrage) {
            if (!v.getMonatlich()) {
                summe = summe.add(BigDecimal.valueOf(v.getPreis()));
            } else {
                summe = summe.add(BigDecimal.valueOf(v.getPreis() * 12));
            }
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
        PreisDTO preisDTO = new PreisDTO();
        // Add any necessary attributes to the model
        model.addAttribute("preisdto", preisDTO);
        return "editPreis";
    }
    @PostMapping("/precalcPreis")
    @ResponseBody
    public Map<String, Object> editPreis(@ModelAttribute PreisDTO preisDTO) {
        double factor = 1.5;
        double factoralter = 0.1;
        double factorspeed = 0.4;
        try (JsonReader reader = Json.createReader(new FileReader(setup.getPreisPath()))) {
            JsonObject jsonObject = reader.readObject();
            factor = jsonObject.getJsonNumber("factor").doubleValue();
            factoralter = jsonObject.getJsonNumber("factorage").doubleValue();
            factorspeed = jsonObject.getJsonNumber("factorspeed").doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigDecimal preis = edit.recalcpricerun(preisDTO.getFaktor(),preisDTO.getAge(), preisDTO.getSpeed(), vertrage);
        Map<String, Object> response = new HashMap<>();
        response.put("preis", preis.setScale(2, RoundingMode.HALF_DOWN) + " €");
        edit.recalcpricerun(factor,factoralter, factorspeed, vertrage);
        return response;
    }

    @PostMapping("/editPreis")
    public String editPreis(
            @ModelAttribute PreisDTO preisDTO,
            Model model) {
        BigDecimal preis = edit.recalcpricerun(preisDTO.getFaktor(),preisDTO.getAge(), preisDTO.getSpeed(), vertrage);
        String confirm = "Preise erfolgreich aktualisiert! neue Preissumme: " + preis.setScale(2, RoundingMode.HALF_DOWN) + "€ pro Jahr";
        model.addAttribute("confirm", confirm);
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
        if (!vertragdto.getPlz().isEmpty()){
            int PLZ = Integer.parseInt(vertragdto.getPlz());
            Partner partner = new Partner(vertragdto.getVorname(), vertragdto.getNachname(), vertragdto.getGender().charAt(0), vertragdto.getBirth(), vertragdto.getLand(), vertragdto.getStrasse(), vertragdto.getHausnummer(), PLZ, vertragdto.getStadt(), vertragdto.getBundesland());
            Fahrzeug fahrzeug = new Fahrzeug(vertragdto.getKennzeichen(), vertragdto.getHersteller(), vertragdto.getTyp(), vertragdto.getSpeed(), vertragdto.getWkz());
            double preis = creator.createPreis(monatlich, partner, fahrzeug);
            response.put("preis", preis + " €");
            return response;
        }
        else{
            response.put("preis", "--,--" + " €");
            return response;
        }
    }

    @PostMapping("/createVertrag")
    public String createVertrag(
            @ModelAttribute VertragDTO vertragdto,
            Model model) {

        boolean monatlich = Objects.equals(vertragdto.getAbrechnung(), "true");
        int plzint = Integer.parseInt(vertragdto.getPlz());
		int vsnr = create.createvsnr()
        Partner partner = new Partner(vertragdto.getVorname(), vertragdto.getNachname(), vertragdto.getGender().charAt(0), vertragdto.getBirth(), vertragdto.getLand(), vertragdto.getStrasse(), vertragdto.getHausnummer(), plzint, vertragdto.getStadt(), vertragdto.getBundesland());
        Fahrzeug fahrzeug = new Fahrzeug(vertragdto.getKennzeichen(), vertragdto.getHersteller(), vertragdto.getTyp(), vertragdto.getSpeed(), vertragdto.getWkz());
        double preis = creator.createPreis(monatlich, partner, fahrzeug);
        Vertrag vertrag = new Vertrag(vsnr, monatlich, preis, vertragdto.getStart(), vertragdto.getEnd(), vertragdto.getCreate(), fahrzeug, partner);
        vertragsverwaltung.vertragAnlegen(vertrag);

        String confirm = "Vertrag mit VSNR "+vsnr+" erfolgreich erstellt! Preis: " + preis + "€";
        model.addAttribute("confirm", confirm);
        return "index";
    }

    @PostMapping("/showDelete")
    public String deleteVertrag(Model model) {
        vertragsverwaltung.vertragLoeschen(vsnr);
        String confirm = "Vertrag erfolgreich gelöscht!";
        model.addAttribute("confirm", confirm);
        return "index";
    }

    @PostMapping("/")
    public String processPrintVertrag(@RequestParam int VSNR, Model model) {
        vsnr = VSNR;
        Vertrag v = vertragsverwaltung.getVertrag(VSNR);
        if (v == null) {
            String result = "Vertrag nicht gefunden!";
            model.addAttribute("result", result);
            return "index";
        }
        model.addAttribute("vsnr", VSNR);
        model.addAttribute("preis", v.getPreis());
        model.addAttribute("abrechnungszeitraumMonatlich", v.getMonatlich());
        model.addAttribute("versicherungsbeginn", v.getVersicherungsbeginn());
        model.addAttribute("versicherungsablauf", v.getVersicherungsablauf());
        model.addAttribute("antragsdatum", v.getAntragsDatum());
        model.addAttribute("kennzeichen", v.getFahrzeug().getAmtlichesKennzeichen());
        model.addAttribute("hersteller", v.getFahrzeug().getHersteller());
        model.addAttribute("typ", v.getFahrzeug().getTyp());
        model.addAttribute("maxspeed", v.getFahrzeug().getHoechstgeschwindigkeit());
        model.addAttribute("wkz", v.getFahrzeug().getWagnisskennziffer());
        model.addAttribute("vorname", v.getPartner().getVorname());
        model.addAttribute("nachname", v.getPartner().getNachname());
        model.addAttribute("geschlecht", v.getPartner().getGeschlecht());
        model.addAttribute("geburtsdatum", v.getPartner().getGeburtsdatum());
        model.addAttribute("strasse", v.getPartner().getStrasse());
        model.addAttribute("hausnummer", v.getPartner().getHausnummer());
        model.addAttribute("plz", v.getPartner().getPlz());
        model.addAttribute("stadt", v.getPartner().getStadt());
        model.addAttribute("bundesland", v.getPartner().getBundesland());
        model.addAttribute("land", v.getPartner().getLand());
        return "handleVertrag";
    }

    @GetMapping("/showVertrag")
    public String showWelcomePage() {
        return "handleVertrag";
    }
}
