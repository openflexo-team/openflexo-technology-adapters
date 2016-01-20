package org.openflexo.technologyadapter.pdf.model;

import java.awt.Point;
import java.awt.Rectangle;

import org.openflexo.foundation.DefaultFlexoObject;

public abstract class AbstractBox extends DefaultFlexoObject {
	private final Rectangle box;

	public AbstractBox(Rectangle box) {
		super();
		this.box = box;
		// System.out.println("Box for [" + text + "] box=" + box + " dir=" + dir);
	}

	public Rectangle getBox() {
		return box;
	}

	public double getX() {
		return box.getX();
	}

	public double getY() {
		return box.getY();
	}

	public double getWidth() {
		return box.getWidth();
	}

	public double getHeight() {
		return box.getHeight();
	}

	@Override
	public String toString() {
		return "Box  box=" + box;
	}

	public void normalizeFrom(PDFDocumentPage from, PDFDocumentPage to) {
		box.x = (int) (box.x * (to.getWidth() / from.getWidth()));
		box.width = (int) (box.width * (to.getWidth() / from.getWidth()));
		box.y = (int) (box.y * (to.getHeight() / from.getHeight()));
		box.height = (int) (box.height * (to.getHeight() / from.getHeight()));
	}

	public void normalizeFrom(PDFDocumentPage from, double fromHMargin, double fromVMargin, PDFDocumentPage to, double toHMargin,
			double toVMargin) {
		/*System.out.println("********** NORMALIZE");
		System.out.println("********** From=" + from.getWidth() / 2.83 + "x" + from.getHeight() / 2.83 + " fromHMargin=" + fromHMargin
				+ " fromVMargin=" + fromVMargin);
		System.out.println("**********   To=" + to.getWidth() / 2.83 + "x" + to.getHeight() / 2.83 + " toHMargin=" + toHMargin
				+ " toVMargin=" + toVMargin);
		System.out.println("Was: " + this);*/
		box.x = (int) ((box.x - fromHMargin * 2.83 / 2) * ((to.getWidth() - toHMargin * 2.83) / (from.getWidth() - fromHMargin * 2.83))
				+ toHMargin * 2.83 / 2);
		box.width = (int) (box.width * ((to.getWidth() - toHMargin * 2.83) / (from.getWidth() - fromHMargin * 2.83)));
		box.y = (int) ((box.y - fromVMargin * 2.83 / 2) * ((to.getHeight() - toVMargin * 2.83) / (from.getHeight() - fromVMargin * 2.83))
				+ toVMargin * 2.83 / 2);
		box.height = (int) (box.height * ((to.getHeight() - toVMargin * 2.83) / (from.getHeight() - fromVMargin * 2.83)));
		// System.out.println("Now: " + this);
	}

	public void extendsTo(float HTolerance, float VTolerance) {
		box.setLocation((int) (box.x - HTolerance * box.width), (int) (box.y - VTolerance * box.height));
		box.setSize((int) (box.width + 2 * HTolerance * box.width), (int) (box.height + 2 * VTolerance * box.height));

	}

	public double distanceFrom(AbstractBox opposite) {
		return distanceFrom(opposite.getBox());
	}

	/**
	 * Compute the distance between two rectangles<br>
	 * Half of the computed distance is obtained from the computation of distance between rectangle centers.<br>
	 * The other half of the computation is obtained from the computation of distance between each corners, one to one.
	 * 
	 * @param opposite
	 * @return
	 */
	public double distanceFrom(Rectangle opposite) {
		Point p11 = new Point((int) getX(), (int) getY());
		Point p12 = new Point((int) (getX() + getWidth()), (int) getY());
		Point p13 = new Point((int) getX(), (int) (getY() + getHeight()));
		Point p14 = new Point((int) (getX() + getWidth()), (int) (getY() + getHeight()));
		Point p1Center = new Point((int) (getX() + getWidth() / 2), (int) (getY() + getHeight() / 2));
		Point p21 = new Point((int) opposite.getX(), (int) opposite.getY());
		Point p22 = new Point((int) (opposite.getX() + opposite.getWidth()), (int) opposite.getY());
		Point p23 = new Point((int) opposite.getX(), (int) (opposite.getY() + opposite.getHeight()));
		Point p24 = new Point((int) (opposite.getX() + opposite.getWidth()), (int) (opposite.getY() + opposite.getHeight()));
		Point p2Center = new Point((int) (opposite.getX() + opposite.getWidth() / 2), (int) (opposite.getY() + opposite.getHeight() / 2));
		return dist(p1Center, p2Center) + (dist(p11, p21) + dist(p12, p22) + dist(p13, p23) + dist(p14, p24)) / 4;
	}

	public double dist(Point p1, Point p2) {
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}

}
