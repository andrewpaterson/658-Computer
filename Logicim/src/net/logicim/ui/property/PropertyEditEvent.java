package net.logicim.ui.property;

import net.logicim.common.type.Int2D;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.input.event.SimulatorEditorEvent;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;

public class PropertyEditEvent
    extends SimulatorEditorEvent
{
  protected StaticView<?> componentView;

  public PropertyEditEvent(StaticView<?> componentView)
  {
    this.componentView = componentView;
  }

  @Override
  public void execute(SimulatorEditor editor)
  {
    componentView.propertyChanged();
    StaticView<?> newComponentView = recreateComponentView(componentView.getProperties(), editor);
    editor.replaceSelection(newComponentView, componentView);
    editor.pushUndo();
  }

  protected StaticView<?> recreateComponentView(ComponentProperties properties, SimulatorEditor editor)
  {
    CircuitEditor circuitEditor = editor.getCircuitEditor();

    Rotation rotation = componentView.getRotation();
    Int2D position = componentView.getPosition();

    Class<? extends StaticView<?>> aClass = (Class<? extends StaticView<?>>) componentView.getClass();
    ViewFactory viewFactory = ViewFactoryStore.getInstance().get(aClass);

    circuitEditor.deleteComponentView(this.componentView);

    StaticView<?> newComponentView = viewFactory.create(circuitEditor, position, rotation, properties);
    circuitEditor.placeComponentView(newComponentView);

    return newComponentView;
  }
}

