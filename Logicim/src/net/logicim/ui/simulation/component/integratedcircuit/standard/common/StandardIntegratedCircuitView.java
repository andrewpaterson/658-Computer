package net.logicim.ui.simulation.component.integratedcircuit.standard.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.defaults.DefaultLogicLevels;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.line.LineView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class StandardIntegratedCircuitView<IC extends IntegratedCircuit<?, ?>, PROPERTIES extends StandardIntegratedCircuitProperties>
    extends IntegratedCircuitView<IC, PROPERTIES>
{
  protected LineView vccLine;
  protected LineView gndLine;

  public StandardIntegratedCircuitView(SubcircuitView subcircuitView,
                                       Circuit circuit,
                                       Int2D position,
                                       Rotation rotation,
                                       PROPERTIES properties)
  {
    super(subcircuitView, circuit, position, rotation, properties);
  }

  protected void createPortViews()
  {
    if (mustIncludeExplicitPowerPorts(FamilyVoltageConfigurationStore.get(properties.family)))
    {
      BoundingBox boundingBox = new BoundingBox();
      updateBoundingBoxFromShapes(boundingBox);

      PortView vccPortView = new PortView(this, integratedCircuit.getVoltageCommon(), new Int2D((int) Math.floor(boundingBox.getLeft()), 0));
      PortView gndPortView = new PortView(this, integratedCircuit.getVoltageGround(), new Int2D((int) Math.ceil(boundingBox.getRight()), 0));

      vccLine = new LineView(this, vccPortView.getRelativePosition(), new Int2D(vccPortView.getRelativePosition().x + 1, vccPortView.getRelativePosition().y));
      gndLine = new LineView(this, gndPortView.getRelativePosition(), new Int2D(gndPortView.getRelativePosition().x - 1, gndPortView.getRelativePosition().y));
    }
  }

  protected List<Port> getPortsInRange(String prefix, int portNumber, int inputWidth)
  {
    ArrayList<Port> ports1 = new ArrayList<>();
    for (int i = portNumber * inputWidth; i < (portNumber + 1) * inputWidth; i++)
    {
      Port port = integratedCircuit.getPort(prefix + i);
      ports1.add(port);
    }
    return ports1;
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
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    if (vccLine != null)
    {
      vccLine.paint(graphics, viewport);
    }

    if (gndLine != null)
    {
      gndLine.paint(graphics, viewport);
    }
  }
}

