package net.logicim.ui.simulation.component.integratedcircuit.standard.counter;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.counter.CounterData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.counter.Counter;
import net.logicim.domain.integratedcircuit.standard.counter.CounterPins;
import net.logicim.domain.integratedcircuit.standard.counter.CounterState;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.common.DetailView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.PortViewCreator;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.PortViewCreatorList;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.CENTER;

public class CounterView
    extends StandardIntegratedCircuitView<Counter, CounterProperties>
{
  protected static int FONT_SIZE = 10;
  protected static int leftOffset = 6;
  protected static int rightOffset = -6;

  protected List<RectangleView> rectangles;
  protected List<PortView> leftPorts;
  protected List<PortView> rightPorts;
  protected List<TextView> labels;

  protected TextView counterValue;

  public CounterView(SubcircuitView containingSubcircuitView,
                     Int2D position,
                     Rotation rotation,
                     CounterProperties properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);

    leftPorts = new ArrayList<>();
    rightPorts = new ArrayList<>();
    labels = new ArrayList<>();
    rectangles = new ArrayList<>();

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
                                -6, 4, 2,
                                new PortViewCreatorList(new PortViewCreator("Clock", true, false),
                                                        new PortViewCreator("Reset", true),
                                                        new PortViewCreator("Load", true),
                                                        new PortViewCreator("Enable"),
                                                        new PortViewCreator("Enable Terminal"),
                                                        new PortViewCreator("Data", properties.bitWidth)),
                                new PortViewCreatorList(new PortViewCreator("Count", properties.bitWidth),
                                                        new PortViewCreator("Terminal Count")));

  }

  @SuppressWarnings("SuspiciousNameCombination")
  protected void createGraphics()
  {
    rectangles.add(new RectangleView(this, 18, rightOffset - leftOffset, true, true));
    createPortLabels(labels,
                     FONT_SIZE,
                     leftPorts,
                     rightPorts,
                     leftOffset - 0.6f,
                     rightOffset + 0.6f);

    float y = -8;
    labels.add(new TextView(this,
                            new Float2D(y, 0),
                            "Up Counter",
                            SANS_SERIF,
                            FONT_SIZE,
                            true,
                            CENTER));

    y = 8;
    counterValue = createDetail(y, 4, "Value", "0");
  }

  @Override
  protected Counter createIntegratedCircuit(ViewPath viewPath,
                                            CircuitSimulation circuitSimulation,
                                            FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    return new Counter(containingSubcircuitSimulation,
                       properties.name,
                       new CounterPins(familyVoltageConfiguration,
                                       properties.bitWidth),
                       properties.bitWidth,
                       properties.terminalValue);
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

    Counter counter = simulationIntegratedCircuits.get(viewPath, circuitSimulation);
    if (counter != null)
    {
      counterValue.setText(counter.getCounterValueHex());
    }

    for (RectangleView rectangle : rectangles)
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

  public CounterData save(boolean selected)
  {
    SimulationStateData<CounterState> stateSimulationStateData = saveSimulationState();
    return new CounterData(position,
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
                           properties.bitWidth,
                           properties.terminalValue);
  }

  @Override
  protected CounterState saveState(Counter integratedCircuit)
  {
    return new CounterState(integratedCircuit.getState().getState());
  }

  private TextView createDetail(float y, int width, String name, String value)
  {
    return DetailView.create(this, labels, rectangles, y, width, name, value, FONT_SIZE);
  }

  @Override
  public String getType()
  {
    return "Counter Oscillator";
  }

  @Override
  public void clampProperties(CounterProperties newProperties)
  {
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Bit-width [%s]\nSet / Terminal value [%s]\n", properties.bitWidth, properties.terminalValue);
  }
}

