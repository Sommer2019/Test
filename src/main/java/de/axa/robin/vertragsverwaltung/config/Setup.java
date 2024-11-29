package de.axa.robin.vertragsverwaltung.config;

public class Setup {
    private final String repositoryPath;
    private final String preisPath;
    private final String brandsPath;
    private final String testURL;
    private final String checkURL;
    private final String host;
    private final int port;

    public Setup() {
        this.repositoryPath = "src/main/resources/static/vertrage.json";
        this.preisPath = "src/main/resources/static/preiscalc.json";
        this.brandsPath = "src/main/resources/static/brands.json";
        this.testURL = "https://www.google.com";
        this.checkURL = "https://nominatim.openstreetmap.org/search?format=json&q=";
        this.host = "localhost";
        this.port = 3128;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public String getPreisPath() {
        return preisPath;
    }

    public String getBrandsPath() {
        return brandsPath;
    }

    public String getTestURL() {
        return testURL;
    }

    public String getCheckURL() {
        return checkURL;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
