package net.synthrose.artofalchemy.gui;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.RegistryEssentia;

public class WEssentiaSubPanel extends WPlainPanel {
	
	private Essentia essentia = null;
	private Integer amount = 0;
	private WSprite sprite = new WSprite(null);
	private WLabel amtLabel = new WLabel("0");
	
	public WEssentiaSubPanel() {
		sprite.setParent(this);
		add(sprite, -4, -4, 54, 18);
		
		amtLabel.setAlignment(Alignment.RIGHT);
		amtLabel.setParent(this);
		add(amtLabel, 8, -4);
	}

	public void setEssentia(Essentia essentia, Integer amount) {
		this.essentia = essentia;
		this.amount = amount;
		Identifier id = RegistryEssentia.INSTANCE.getId(essentia);
		if (essentia != null) {
			sprite.setImage(new Identifier(id.getNamespace(), "textures/gui/essentia_banner/"
					+ id.getPath() +".png"));
			amtLabel.setText(new LiteralText(amount.toString()));
		} else {
			sprite.setImage(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/essentia_banner/empty.png"));
			amtLabel.setText(new TranslatableText("gui." + ArtOfAlchemy.MOD_ID + ".empty"));
			amtLabel.setAlignment(Alignment.LEFT);
		}
		this.layout();
	}
	
	public Essentia getEssentia() {
		return essentia;
	}
	
	public Integer getAmount() {
		return amount;
	}

}
