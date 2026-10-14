package community.harpyvents;

/** Item identity only: possession never grants or requires a role. */
public final class VentAccess {
    public static final String LOCKPICK = "wathe:lockpick";
    public static final String MASTER_KEY = "noellesroles:master_key";

    private VentAccess() {}

    public static boolean isKey(String registryId) {
        return LOCKPICK.equals(registryId) || MASTER_KEY.equals(registryId);
    }

    public static boolean canToggle(boolean open, String mainHandId, String offHandId) {
        // Closing an already open hatch requires no key.
        return open || isKey(mainHandId) || isKey(offHandId);
    }
}
