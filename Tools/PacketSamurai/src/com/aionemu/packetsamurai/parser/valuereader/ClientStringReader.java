/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */


package com.aionemu.packetsamurai.parser.valuereader;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.dataholders.ClientStrings;
import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.StringValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

public class ClientStringReader implements Reader {

	private static ClientStrings _data;

	static {
		PacketSamurai.getUserInterface().log("Loading Client strings... Please wait.");

		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			schema = sf.newSchema(new File("./data/client_strings.xsd"));
			File xml = new File("./data/client_strings.xml");

			JAXBContext jc;
			Unmarshaller unmarshaller;
			jc = JAXBContext.newInstance(ClientStrings.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			_data = (ClientStrings) unmarshaller.unmarshal(xml);
		}
		catch (SAXException se) {
			PacketSamurai.getUserInterface().log("ERROR: Failed to load client_strings.xsd: " + se.toString());
		}
		catch (JAXBException je) {
			PacketSamurai.getUserInterface().log("ERROR: Failed to load client_strings.xml: " + je.toString());
		}
	}

	boolean _real = true;

	@Override
	public boolean loadReaderFromXML(Node n) {
		NamedNodeMap enumAttrs = n.getAttributes();
		Node nameAttr = enumAttrs.getNamedItem("real");
		if (nameAttr != null) {
			_real = Boolean.parseBoolean(nameAttr.getNodeValue());
		}

		return true;
	}

	@Override
	public boolean saveReaderToXML(Element element, Document doc) {
		return true;
	}

	@Override
	public JComponent readToComponent(ValuePart part) {
		return new JLabel(read(part));
	}

	@Override
	public String read(ValuePart part) {
		int id = 0;
		if (!(part instanceof IntValuePart)) {
			if (!(part instanceof StringValuePart))
				return "";
			try {
				id = Integer.parseInt(((StringValuePart) part).getStringValue());
			}
			catch (NumberFormatException e) {
				return ((StringValuePart) part).getStringValue();
			}
		}
		else {
			id = ((IntValuePart) part).getIntValue();
		}

		if (!_real && id % 2 == 1)
			id = (id - 1) / 2;

		if (_data == null)
			return Integer.toString(id);

		return _data.getStringById(id);
	}

	public static ClientStrings getData() {
		return _data;
	}

}
