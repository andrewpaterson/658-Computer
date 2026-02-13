package net.logicim.ui.simulation.component.integratedcircuit.standard.common;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.defaults.DefaultLogicLevels;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.passive.power.PowerPinNames;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.text.TextView;

import java.awt.*;
import java.util.List;

import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.LEFT;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.RIGHT;

public abstract class StandardIntegratedCircuitView<IC extends IntegratedCircuit<?, ?>, PROPERTIES extends StandardIntegratedCircuitProperties>
    extends IntegratedCircuitView<IC, PROPERTIES>
{
  protected LineView vccLine;
  protected LineView gndLine;

  public StandardIntegratedCircuitView(SubcircuitView containingSubcircuitView,
                                       Int2D position,
                                       Rotation rotation,
                                       PROPERTIES properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
  }

  protected void createPortViews()
  {
    if (mustIncludeExplicitPowerPorts(FamilyVoltageConfigurationStore.get(properties.family)))
    {
      BoundingBox boundingBox = new BoundingBox();
      updateBoundingBoxFromShapes(boundingBox);

      PortView vccPortView = new PortView(this, PowerPinNames.VCC, new Int2D((int) Math.floor(boundingBox.getLeft()), 0));
      PortView gndPortView = new PortView(this, PowerPinNames.GND, new Int2D((int) Math.ceil(boundingBox.getRight()), 0));

      vccLine = new LineView(this, vccPortView.getRelativePosition(), new Int2D(vccPortView.getRelativePosition().x + 1, vccPortView.getRelativePosition().y));
      gndLine = new LineView(this, gndPortView.getRelativePosition(), new Int2D(gndPortView.getRelativePosition().x - 1, gndPortView.getRelativePosition().y));
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  protected void createLeftAndRightPortViews(List<PortView> leftViews,
                                             List<PortView> rightViews,
                                             int leftOffset,
                                             int rightOffset,
                                             int leftYStart,
                                             int rightYStart,
                                             int yStep,
                                             PortViewCreatorList leftCreators,
                                             PortViewCreatorList rightCreators)
  {
    int y = leftYStart;
    for (PortViewCreator creator : leftCreators.creators)
    {
      leftViews.add(createPortView(y,
                                   creator.name,
                                   creator.text,
                                   leftOffset,
                                   creator.portCount,
                                   creator.clock,
                                   creator.inverted,
                                   Rotation.South));
      y += yStep;
    }

    y = rightYStart;
    for (PortViewCreator creator : rightCreators.creators)
    {

      rightViews.add(createPortView(y,
                                    creator.name,
                                    creator.text,
                                    rightOffset,
                                    creator.portCount,
                                    creator.clock,
                                    creator.inverted,
                                    Rotation.North));
      y += yStep;
    }
  }

  private PortView createPortView(int x,
                                  String portName,
                                  String text,
                                  int y,
                                  int portCount,
                                  boolean clock,
                                  boolean inverted,
                                  Rotation rotation)
  {
    PortView portView;
    if (portCount == 1)
    {
      portView = new PortView(this,
                              portName,
                              new Int2D(x, y));
      portView.setText(text.trim());
    }
    else
    {
      portView = new PortView(this,
                              getPortNames(portName,
                                           0,
                                           portCount,
                                           1),
                              new Int2D(x, y));
      portView.setText(text);
    }
    if (clock)
    {
      portView.setDrawClock(true);
    }
    if (inverted)
    {
      portView.setInverting(true, rotation);
    }
    return portView;
  }

  @SuppressWarnings("SuspiciousNameCombination")
  protected void createPortLabels(List<TextView> labels,
                                  int fontSize,
                                  List<PortView> leftPorts,
                                  List<PortView> rightPorts,
                                  float leftOffset,
                                  float rightOffset)
  {
    for (PortView portView : leftPorts)
    {
      labels.add(new TextView(this,
                              new Float2D(portView.getRelativePosition().x, leftOffset),
                              portView.getText(),
                              SANS_SERIF,
                              fontSize,
                              false,
                              LEFT));
    }

    for (PortView portView : rightPorts)
    {
      labels.add(new TextView(this,
                              new Float2D(portView.getRelativePosition().x, rightOffset),
                              portView.getText(),
                              SANS_SERIF,
                              fontSize,
                              false,
                              RIGHT));
    }
  }

  @Override
  protected boolean mustIncludeExplicitPowerPorts(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    if (properties.explicitPowerPorts)
    {
      return true;
    }
    else
    {
      VoltageConfiguration voltageConfiguration = familyVoltageConfiguration.getDefaultVoltageConfiguration(DefaultLogicLevels.get());
      if (voltageConfiguration == null)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
  {
    super.paint(graphics,
                viewport,
                viewPath,
                circuitSimulation);

    if (vccLine != null)
    {
      vccLine.paint(graphics, viewport);
    }

    if (gndLine != null)
    {
      gndLine.paint(graphics, viewport);
    }
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Name [%s]\nFamily [%s]\nExplicit Power [%s]\n", properties.name, properties.family.toString(), properties.explicitPowerPorts);
  }
}

