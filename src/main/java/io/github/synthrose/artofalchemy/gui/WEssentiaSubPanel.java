package io.github.synthrose.artofalchemy.gui;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.essentia.Essentia;
import io.github.synthrose.artofalchemy.essentia.RegistryEssentia;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public class WEssentiaSubPanel extends WPlainPanel {
	
	private Essentia essentia = null;
	private Integer amount = 0;
	private Integer required = 0;
	private final WSprite bg = new WSprite(ArtOfAlchemy.id("textures/gui/essentia_banner.png"));
	private final WSprite sprite = new WSprite(null);
	private final WLabel amtLabel = new WLabel("0");
	private final WLabel typeLabel = new WLabel("Empty");
	
	public WEssentiaSubPanel() {
		bg.setParent(this);
		add(bg, -4, -4, 54, 18);
		
		sprite.setParent(this);
		add(sprite, 25, -4, 18, 18);
		
		amtLabel.setAlignment(Alignment.RIGHT);
		amtLabel.setParent(this);
		add(amtLabel, 8, -4);
		
		typeLabel.setAlignment(Alignment.LEFT);
		typeLabel.setParent(this);
		add(typeLabel, -3, 5);
	}

	public void setEssentia(Essentia essentia, Integer amount) {
		this.essentia = essentia;
		this.amount = amount;
		Identifier id = RegistryEssentia.INSTANCE.getId(essentia);
		if (essentia != null) {
			bg.setTint(essentia.getColor());
			sprite.setImage(new Identifier(id.getNamespace(), "textures/gui/symbols/" + id.getPath() +".png"));
			amtLabel.setText(new LiteralText(amount.toString()));
			typeLabel.setText(new TranslatableText("essentia." + id.getNamespace() + "." + id.getPath()));
			typeLabel.setColor(0xFFFFFF, 0xFFFFFF);
		} else {
			sprite.setImage(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/symbols/empty.png"));
			typeLabel.setText(new TranslatableText("gui." + ArtOfAlchemy.MOD_ID + ".empty"));
			amtLabel.setText(new LiteralText(""));
		}
		amtLabel.setColor(WLabel.DEFAULT_TEXT_COLOR, WLabel.DEFAULT_DARKMODE_TEXT_COLOR);
		this.layout();
	}
	
	public void setEssentia(Essentia essentia, Integer amount, Integer required) {
		setEssentia(essentia, amount);
		this.required = required;
		if (required > 0) {
			if (amount < required) {
				amtLabel.setColor(0xAA0000, 0xFF5555);
				amtLabel.setText(new LiteralText(Integer.toString(amount - required)));
			} else {
				amtLabel.setColor(0x00AA00, 0x55FF55);
				amtLabel.setText(new LiteralText("+" + (amount - required)));
			}
		}
	}
	
	public Essentia getEssentia() {
		return essentia;
	}
	
	public Integer getAmount() {
		return amount;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addInformation(List<String> information) {
		if (essentia != null) {
			Identifier id = RegistryEssentia.INSTANCE.getId(essentia);
			String essentiaName = I18n.translate("fluid." + id.getNamespace() + ".essentia_" + id.getPath());
			String amtTip;
			if (required > 0) {
				amtTip = I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".essentia_amount", amount, required);
			} else {
				amtTip = amount.toString();
			}
			information.add(essentiaName);
			information.add(amtTip);
		} else {
			information.add(I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".empty"));
		}
		
		super.addInformation(information);
	}
	
	

}
