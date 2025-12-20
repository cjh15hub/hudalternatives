public class MinimalModernOverlay {

    private static boolean enabled = true;

    public static void toggle() {
        enabled = !enabled;
    }

    public static final IGuiOverlay HUD = new IGuiOverlay() {
        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {

            if (!enabled) return;

            final LocalPlayer player = gui.getMinecraft().player;
            if (player == null) return;

            // ⬇️ SEMUA KODE RENDER KAMU YANG LAMA TETAP DI SINI
            // (tidak perlu diubah satu baris pun)
        }
    };

    // SEMUA METHOD LAIN BIARKAN TETAP
}
