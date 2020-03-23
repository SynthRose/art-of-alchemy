package net.synthrose.artofalchemy.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import io.github.cottonmc.cotton.gui.widget.WListPanel;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.essentia.EssentiaStack;

public class WEssentiaPanel extends WListPanel<Essentia, WEssentiaSubPanel> {
	
	protected EssentiaContainer container = new EssentiaContainer();
	protected EssentiaStack required = new EssentiaStack();
		
	public WEssentiaPanel(EssentiaContainer container) {
		super(new ArrayList<Essentia>(), null, null);
		this.container = container;
		this.supplier = WEssentiaSubPanel::new;
		this.configurator = null;
		updateEssentia(container);
	}
	
	public WEssentiaPanel(EssentiaContainer container, EssentiaStack required) {
		super(new ArrayList<Essentia>(), null, null);
		this.container = container;
		this.required = required;
		this.supplier = WEssentiaSubPanel::new;
		this.configurator = null;
		updateEssentia(container, required);
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
	
	public void updateEssentia(EssentiaContainer container, EssentiaStack required) {
		this.container = container;
		this.required = required;
		this.configurator = (Essentia essentia, WEssentiaSubPanel panel) -> {
			panel.setEssentia(essentia, container.getCount(essentia),
					required.getOrDefault(essentia, 0));
		};
		rebuildList();
		reconfigure();
		this.layout();
	}
	
	protected void rebuildList() {
		Set<Essentia> essentiaSet = new HashSet<>();
		essentiaSet.addAll(container.getContents().keySet());
		essentiaSet.addAll(required.keySet());
		
		Map<Essentia, Integer> sortOrder = new HashMap<>();
		for (Essentia key : essentiaSet) {
			int value;
			if (required.getOrDefault(key, 0) > 0) {
				value = 10000 + (required.get(key)) - container.getCount(key);
			} else {
				value = container.getCount(key);
			}
			if (value != 0) {
				sortOrder.put(key, value);
			}
		}
		
		data.clear();
		data.addAll(sortOrder.keySet());
		data.sort((key1, key2) -> {
			return sortOrder.get(key2) - sortOrder.get(key1);
		});
		
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
