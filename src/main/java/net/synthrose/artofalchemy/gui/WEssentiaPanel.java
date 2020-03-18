package net.synthrose.artofalchemy.gui;

import java.util.ArrayList;
import java.util.Map.Entry;

import io.github.cottonmc.cotton.gui.widget.WListPanel;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;

public class WEssentiaPanel extends WListPanel<Essentia, WEssentiaSubPanel> {
	
	protected EssentiaContainer container = new EssentiaContainer();
		
	public WEssentiaPanel(EssentiaContainer container) {
		super(new ArrayList<Essentia>(), null, null);
		this.container = container;
		this.supplier = WEssentiaSubPanel::new;
		this.configurator = (Essentia essentia, WEssentiaSubPanel panel) -> {
			panel.setEssentia(essentia, container.getCount(essentia));
		};
		updateEssentia(container);
	}
	
	public WEssentiaPanel() {
		this(new EssentiaContainer());
	}
	
	public void updateEssentia(EssentiaContainer container) {
		this.container = container;
		this.configurator = (Essentia essentia, WEssentiaSubPanel panel) -> {
			panel.setEssentia(essentia, container.getCount(essentia));
		};
		rebuildList();
		reconfigure();
		this.layout();
	}
	
	protected void rebuildList() {
		data.clear();
		data.addAll(container.getContents().sortedList());
		if (data.isEmpty()) {
			data.add(null);
		}
	}
	
	protected void reconfigure() {
		for (Entry<Essentia, WEssentiaSubPanel> entry : configured.entrySet()) {
			configurator.accept(entry.getKey(), entry.getValue());
		}
	}

}
