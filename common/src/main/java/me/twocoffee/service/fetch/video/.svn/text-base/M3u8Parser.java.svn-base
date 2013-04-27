package me.twocoffee.service.fetch.video;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import me.twocoffee.entity.M3u8;
import me.twocoffee.entity.M3u8.Element;

public class M3u8Parser {
	public M3u8 parse(String url) {
		URI uri = URI.create(url);
		try {
			InputStream in = uri.toURL().openStream();
			Playlist pl = Playlist.parse(in);
			if (pl == null || pl.getElements() == null || pl.getElements().size() < 1)
				return null;
			
			M3u8 m = new M3u8();
			m.setUrl(url);
			m.setElements(getElements(pl.getElements()));
			return m;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Element> getElements(
			List<me.twocoffee.service.fetch.video.Element> elements) {
		List<Element> list = new ArrayList();
		for (me.twocoffee.service.fetch.video.Element e : elements) {
			Element elt = new Element();
			elt.setDuration(e.getDuration());
			elt.setUrl(e.getURI().toString());
			
			list.add(elt);
		}
		return list;
	}
}
