package me.pixeldots.pixelscharactermodels.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ToggleWidget extends ClickableWidget {

    public boolean toggled = false;
    public ToggleAction on_toggle;
    public String base_message;

    public ToggleWidget(int x, int y, int width, int height, String message, boolean _toggled,  ToggleAction _ontoggle) {
        super(x, y, width, height, Text.of((_toggled ? "[X]" : "[ ]") + message));
        this.base_message = message;
        this.on_toggle = _ontoggle;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        /*RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);*/
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        ClickableWidget.drawTextWithShadow(matrices, textRenderer, this.getMessage(), this.x + 4, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder var1) {}
    
    @Override
    public void onClick(double mouseX, double mouseY) {
        toggled = !toggled;
        this.setMessage(Text.of((toggled ? "[X]" : "[ ]") + base_message));
        on_toggle.invoke(toggled);
    }

    public interface ToggleAction {
        void invoke(boolean toggled);
    }

}
