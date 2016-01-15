package org.openflexo.technologyadapter.pdf.model;

import java.awt.Point;
import java.awt.Rectangle;

import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.technologyadapter.pdf.PDFTechnologyAdapter;

public abstract class AbstractBox extends DefaultFlexoObject {
	private final Rectangle box;

	public AbstractBox( Rectangle box) {
		super();
		this.box = box;
		// System.out.println("Box for [" + text + "] box=" + box + " dir=" + dir);
	}

	public Rectangle getBox() {
		return box;
	}

	public double getX() {
		/*if (dir == 90 || dir == 270) {
			return box.getX() - box.getWidth();
		}*/
		return box.getX();
	}

	public double getY() {
		/*if (dir == 0 || dir == 180) {
			return box.getY() - box.getHeight();
		}*/
		return box.getY();
	}

	public double getWidth() {
		/*if (dir == 90 || dir == 270) {
			return box.getWidth() * 1.1;
		}
		else {*/
		return box.getWidth();
		// }
	}

	public double getHeight() {
		/*if (dir == 0 || dir == 180) {
			return box.getHeight() * 1.1;
		}
		else {*/
		return box.getHeight();
		// }
	}

	@Override
	public String toString() {
		return "Box  box=" + box ;
	}

	public void normalizeFrom(PDFDocumentPage from, PDFDocumentPage to) {
		box.x = (int) (box.x * (to.getWidth() / from.getWidth()));
		box.width = (int) (box.width * (to.getWidth() / from.getWidth()));
		box.y = (int) (box.y * (to.getHeight() / from.getHeight()));
		box.height = (int) (box.height * (to.getHeight() / from.getHeight()));
	}

	public double distanceFrom(AbstractBox opposite) {
		Point p11 = new Point((int) getX(), (int) getY());
		Point p12 = new Point((int) (getX() + getWidth()), (int) getY());
		Point p13 = new Point((int) getX(), (int) (getY() + getHeight()));
		Point p14 = new Point((int) (getX() + getWidth()), (int) (getY() + getHeight()));
		Point p21 = new Point((int) opposite.getX(), (int) opposite.getY());
		Point p22 = new Point((int) (opposite.getX() + opposite.getWidth()), (int) opposite.getY());
		Point p23 = new Point((int) opposite.getX(), (int) (opposite.getY() + opposite.getHeight()));
		Point p24 = new Point((int) (opposite.getX() + opposite.getWidth()), (int) (opposite.getY() + opposite.getHeight()));
		return dist(p11, p21) + dist(p12, p22) + dist(p13, p23) + dist(p14, p24);
	}

	public double dist(Point p1, Point p2) {
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}
}
