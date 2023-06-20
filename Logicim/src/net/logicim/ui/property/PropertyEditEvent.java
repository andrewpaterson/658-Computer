package net.logicim.ui.property;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.input.event.SimulatorEditorEvent;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public class PropertyEditEvent
    extends SimulatorEditorEvent
{
  protected StaticView<ComponentProperties> componentView;
  protected ComponentProperties newComponentProperties;
  protected SubcircuitEditor subcircuitEditor;
  protected Circuit circuit;

  public PropertyEditEvent(StaticView<?> componentView,
                           ComponentProperties newComponentProperties,
                           SubcircuitEditor subcircuitEditor,
                           Circuit circuit)
  {
    this.componentView = (StaticView<ComponentProperties>) componentView;
    this.newComponentProperties = newComponentProperties;
    this.subcircuitEditor = subcircuitEditor;
    this.circuit = circuit;
  }

  @Override
  public void execute(Logicim editor)
  {
    componentView.clampProperties(newComponentProperties);
    StaticView<?> newComponentView = recreateComponentView(newComponentProperties, editor.getCircuitEditor());
    editor.replaceSelectionInCurrentSubcircuitView(newComponentView, componentView);

    editor.getCircuitEditor().circuitUpdated();
    editor.pushUndo();
    editor.updateHighlighted();
  }

  protected StaticView<?> recreateComponentView(ComponentProperties properties, CircuitEditor circuitEditor)
  {
    circuitEditor.deleteComponentView(this.componentView);
    StaticView<?> staticView = componentView.duplicate(circuitEditor,
                                                       circuitEditor.getCurrentSubcircuitView(),
                                                       properties);

    circuitEditor.recreateComponentView(staticView);

    return staticView;
  }
}

