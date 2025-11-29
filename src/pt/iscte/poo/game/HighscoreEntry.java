package pt.iscte.poo.game;

public class HighscoreEntry {

    private final String name;
    private final long timeMillis;
    private final int moves;

    public HighscoreEntry(String name, long timeMillis, int moves) {
        this.name = name;
        this.timeMillis = timeMillis;
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public int getMoves() {
        return moves;
    }

    public long getTimeSeconds() {
        return Math.max(0, timeMillis / 1000);
    }

    public String serialize() {
        String safeName = name.replace(";", ",").trim();
        return safeName + ";" + timeMillis + ";" + moves;
    }

    public static HighscoreEntry fromLine(String line) {
        if (line == null || line.isBlank())
            return null;
        String[] parts = line.split(";");
        if (parts.length != 3)
            return null;
        try {
            String name = parts[0].trim();
            long time = Long.parseLong(parts[1].trim());
            int moves = Integer.parseInt(parts[2].trim());
            return new HighscoreEntry(name, time, moves);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
