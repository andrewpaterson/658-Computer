package net.logicim.ui.debugdetail;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;

import java.awt.*;

public class ComponentInformationPanel
    extends InformationPanel
{
  private StaticView<?> componentView;

  public ComponentInformationPanel(StaticView<?> componentView, Graphics2D graphics, Viewport viewport, int width, int height)
  {
    super(graphics, viewport, width, height);
    this.componentView = componentView;
  }

  @Override
  protected void paintDetail(SubcircuitSimulation subcircuitSimulation, int fontHeight, int x, int y)
  {

    y = drawMultilineString(fontHeight, x, y, " < " + componentView.getClass().getSimpleName() + " >");
    drawMultilineString(fontHeight, x, y, componentView.toDebugString());
  }
}
