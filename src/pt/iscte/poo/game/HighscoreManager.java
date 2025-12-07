package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class HighscoreManager {

    private static final String SCORE_FILE = "highscores.txt";
    private static final int MAX_ENTRIES = 10;
    private static final HighscoreManager INSTANCE = new HighscoreManager();

    private HighscoreManager() {
    }

    public static HighscoreManager getInstance() {
        return INSTANCE;
    }

    public synchronized void recordScore(String name, long timeMillis, int moves) {
        List<HighscoreEntry> entries = loadScores();
        entries.add(new HighscoreEntry(name, timeMillis, moves));
        entries.sort(Comparator
                .comparingLong(HighscoreEntry::getTimeMillis)
                .thenComparingInt(HighscoreEntry::getMoves));
        while (entries.size() > MAX_ENTRIES)
            entries.remove(entries.size() - 1);
        saveScores(entries);
    }

    public synchronized String formatHighscores() {
        List<HighscoreEntry> entries = loadScores();
        if (entries.isEmpty())
            return "Ainda não existem highscores.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-15s %-10s %-8s%n", "#", "Jogador", "Tempo", "Movs"));
        sb.append("-------------------------------------").append(System.lineSeparator());

        int pos = 1;
        for (HighscoreEntry entry : entries) {
            sb.append(String.format("%-4d %-15s %-10s %-8d%n",
                    pos++,
                    entry.getName(),
                    formatTime(entry.getTimeMillis()),
                    entry.getMoves()));
        }

        return sb.toString();
    }

    private List<HighscoreEntry> loadScores() {
        List<HighscoreEntry> entries = new ArrayList<>();
        File file = new File(SCORE_FILE);
        if (!file.exists())
            return entries;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                HighscoreEntry entry = HighscoreEntry.fromLine(sc.nextLine());
                if (entry != null)
                    entries.add(entry);
            }
        } catch (FileNotFoundException e) {
            
        }
        return entries;
    }

    private void saveScores(List<HighscoreEntry> entries) {
        File file = new File(SCORE_FILE);
        try (PrintWriter out = new PrintWriter(file)) {
            for (HighscoreEntry entry : entries) {
                out.println(entry.serialize());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Não foi possível gravar highscores: " + e.getMessage());
        }
    }

    private String formatTime(long timeMillis) {
        long totalSeconds = Math.max(0, timeMillis / 1000);
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
