package net.logicim.ui.property;

import net.logicim.domain.common.Circuit;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.input.event.SimulatorEditorEvent;
import net.logicim.ui.simulation.CircuitEditor;

public class PropertyEditEvent
    extends SimulatorEditorEvent
{
  protected StaticView<ComponentProperties> componentView;
  protected ComponentProperties newComponentProperties;
  protected SubcircuitView subcircuitView;
  protected Circuit circuit;

  public PropertyEditEvent(StaticView<?> componentView,
                           ComponentProperties newComponentProperties,
                           SubcircuitView subcircuitView,
                           Circuit circuit)
  {
    this.componentView = (StaticView<ComponentProperties>) componentView;
    this.newComponentProperties = newComponentProperties;
    this.subcircuitView = subcircuitView;
    this.circuit = circuit;
  }

  @Override
  public void execute(SimulatorEditor editor)
  {
    componentView.clampProperties(newComponentProperties);
    StaticView<?> newComponentView = recreateComponentView(newComponentProperties, editor);
    editor.replaceSelectionInCurrentSubcircuitView(newComponentView, componentView);
    editor.pushUndo();
  }

  protected StaticView<?> recreateComponentView(ComponentProperties properties, SimulatorEditor editor)
  {
    CircuitEditor circuitEditor = editor.getCircuitEditor();
    StaticView<?> newComponentView = componentView.duplicate(subcircuitView, circuit, properties);
    circuitEditor.deleteComponentView(this.componentView);
    circuitEditor.placeComponentView(newComponentView);

    return newComponentView;
  }
}

