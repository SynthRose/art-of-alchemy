package net.synthrose.artofalchemy.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import io.github.cottonmc.cotton.gui.widget.WListPanel;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.essentia.EssentiaStack;

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
		System.out.println(container.toTag());
		rebuildList();
		reconfigure();
		this.layout();
	}
	
	protected void rebuildList() {
		data.clear();
		data.addAll(buildList(container));
	}
	
	protected void reconfigure() {
		for (Entry<Essentia, WEssentiaSubPanel> entry : configured.entrySet()) {
			configurator.accept(entry.getKey(), entry.getValue());
		}
	}
	
	protected static List<Essentia> buildList(EssentiaContainer container) {
		List<Essentia> list = new ArrayList<>();
		EssentiaStack essentia = container.getContents();
		for (Essentia key : essentia.keySet()) {
			if (essentia.get(key) > 0 && !list.contains(key)) {
				list.add(key);
			} else if (essentia.getOrDefault(key, 0) <= 0 && list.contains(key)) {
				list.remove(key);
			}
		}
		list.sort((item1, item2) -> {
			return essentia.get(item2) - essentia.get(item1);
		});
		return list;
	}

}
