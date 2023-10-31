package da2i.payetesdettes.utils;

import java.util.List;

public class DisplayUtil <T> {

	public String displayList (List<T> list) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < list.size(); i++) {
			T t = list.get(i);
			if (i+1 == list.size()) {
				sb.append(t.toString());
			} else {
				sb.append(t.toString()+" ; ");
			}
		}
		return sb.toString();
	}
}
