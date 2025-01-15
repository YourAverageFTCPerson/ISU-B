package average.ftc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public class ConfigParser {
    private static final System.Logger LOGGER = System.getLogger(ConfigParser.class.getName());

    @SuppressWarnings("unused") // used by Jackson
    public static class Config {
        private int enemies, friendlies;

        private String mapLoc;

        private double money;

        public int getEnemies() {
            return enemies;
        }

        public void setEnemies(String enemies) {
            this.enemies = Integer.parseInt(enemies);
        }

        public int getFriendlies() {
            return friendlies;
        }

        public void setFriendlies(String friendlies) {
            this.friendlies = Integer.parseInt(friendlies);
        }

        public String getMapLoc() {
            return mapLoc;
        }

        public void setMapLoc(String mapLoc) {
            this.mapLoc = mapLoc;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = Double.parseDouble(money);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Config config)) return false;
            return enemies == config.enemies && friendlies == config.friendlies && Double.compare(money, config.money) == 0 && Objects.equals(mapLoc, config.mapLoc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(enemies, friendlies, mapLoc, money);
        }

        @Override
        public String toString() {
            return "Config{" +
                    "enemies=" + enemies +
                    ", friendlies=" + friendlies +
                    ", mapLoc='" + mapLoc + '\'' +
                    ", money=" + money +
                    '}';
        }
    }

    public static Config readConfig() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                            "config.json"), Config.class);
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error while parsing config", e);
            System.exit(1);
            throw new AssertionError();
        }
    }
}
