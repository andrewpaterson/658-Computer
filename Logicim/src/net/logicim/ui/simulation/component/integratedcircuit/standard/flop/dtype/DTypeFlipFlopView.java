package net.logicim.ui.simulation.component.integratedcircuit.standard.flop.dtype;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.flop.dtype.DTypeFlipFlopData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.flop.dtype.DTypeFlipFlop;
import net.logicim.domain.integratedcircuit.standard.flop.dtype.DTypeFlipFlopPins;
import net.logicim.domain.integratedcircuit.standard.flop.dtype.DTypeFlipFlopState;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.PortViewCreator;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.PortViewCreatorList;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DTypeFlipFlopView
    extends StandardIntegratedCircuitView<DTypeFlipFlop, DTypeFlipFlopProperties>
{
  protected static int FONT_SIZE = 10;
  protected static int leftOffset = 3;
  protected static int rightOffset = -3;

  protected RectangleView rectangle;
  protected java.util.List<PortView> leftPorts;
  protected List<PortView> rightPorts;
  protected List<TextView> labels;

  public DTypeFlipFlopView(SubcircuitView containingSubcircuitView,
                           Int2D position,
                           Rotation rotation,
                           DTypeFlipFlopProperties properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);

    leftPorts = new ArrayList<>();
    rightPorts = new ArrayList<>();
    labels = new ArrayList<>();

    createPortViews();
    createGraphics();
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createLeftAndRightPortViews(leftPorts, rightPorts,
                                leftOffset, rightOffset,
                                -3, -1, 2,
                                new PortViewCreatorList(new PortViewCreator("CP", true, false),
                                                        new PortViewCreator("SD", true),
                                                        new PortViewCreator("D"),
                                                        new PortViewCreator("RD", true)),
                                new PortViewCreatorList(new PortViewCreator("Q"),
                                                        new PortViewCreator("Q", "QB", true)));

  }

  protected void createGraphics()
  {
    rectangle = new RectangleView(this, 8, rightOffset - leftOffset, true, true);
    createPortLabels(labels,
                     FONT_SIZE,
                     leftPorts,
                     rightPorts,
                     leftOffset - 0.6f,
                     rightOffset + 0.6f);

  }

  @Override
  protected DTypeFlipFlop createIntegratedCircuit(ViewPath viewPath,
                                                  CircuitSimulation circuitSimulation,
                                                  FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    return new DTypeFlipFlop(containingSubcircuitSimulation,
                             properties.name,
                             new DTypeFlipFlopPins(familyVoltageConfiguration,
                                                   properties.inverseOut,
                                                   properties.setReset));
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
  {
    super.paint(graphics, viewport, viewPath, circuitSimulation);
    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();
    Font font = graphics.getFont();

    if (rectangle != null)
    {
      rectangle.paint(graphics, viewport);
    }

    for (TextView label : labels)
    {
      label.paint(graphics, viewport);
    }

    paintPorts(graphics, viewport, viewPath, circuitSimulation);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  public DTypeFlipFlopData save(boolean selected)
  {
    SimulationStateData<DTypeFlipFlopState> stateSimulationStateData = saveSimulationState();
    return new DTypeFlipFlopData(position,
                                 rotation,
                                 properties.name,
                                 properties.family,
                                 saveEvents(),
                                 savePorts(),
                                 id,
                                 enabled,
                                 selected,
                                 stateSimulationStateData,
                                 properties.explicitPowerPorts,
                                 properties.inverseOut,
                                 properties.setReset);
  }

  @Override
  protected DTypeFlipFlopState saveState(DTypeFlipFlop integratedCircuit)
  {
    return new DTypeFlipFlopState(integratedCircuit.getState().getState());
  }

  @Override
  public String getType()
  {
    return "DTypeFlipFlop Oscillator";
  }

  @Override
  public void clampProperties(DTypeFlipFlopProperties newProperties)
  {
//    newProperties.frequency_Hz = clamp(newProperties.frequency_Hz, 0, 999 * GHz);
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Inverse Out [%s]\nSet / Reset [%s]\n", properties.inverseOut, properties.setReset);
  }
}

