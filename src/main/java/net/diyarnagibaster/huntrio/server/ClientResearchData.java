package net.diyarnagibaster.huntrio.server;

public class ClientResearchData {
    private static final java.util.Set<String> UNLOCKED = new java.util.HashSet<>();

    public static void update(java.util.List<String> ids) {
        UNLOCKED.clear();
        UNLOCKED.addAll(ids);
    }

    public static boolean isUnlocked(String id) {
        return UNLOCKED.contains(id);
    }
}