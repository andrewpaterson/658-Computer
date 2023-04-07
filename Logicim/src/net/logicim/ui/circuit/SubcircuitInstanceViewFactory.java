package net.logicim.ui.circuit;

import net.logicim.common.type.Int2D;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import javax.swing.*;

public class SubcircuitInstanceViewFactory
    extends ViewFactory<SubcircuitInstanceView, SubcircuitInstanceProperties>
{
  protected String subcircuitTypeName;

  public SubcircuitInstanceViewFactory()
  {
    subcircuitTypeName = "";
  }

  @Override
  public SubcircuitInstanceView create(CircuitEditor circuitEditor,
                                       Int2D position,
                                       Rotation rotation)
  {
    SubcircuitInstanceProperties defaultProperties = createDefaultProperties(getViewClass());
    defaultProperties.subcircuitTypeName = subcircuitTypeName;
    return create(circuitEditor,
                  position,
                  rotation,
                  defaultProperties);
  }

  public void setSubcircuitTypeName(String subcircuitTypeName)
  {
    this.subcircuitTypeName = subcircuitTypeName;
  }

  @Override
  public SubcircuitInstanceProperties createInitialProperties()
  {
    return new SubcircuitInstanceProperties("",
                                            "",
                                            "",
                                            0,
                                            0);
  }

  @Override
  public SubcircuitInstanceView create(CircuitEditor circuitEditor,
                                       Int2D position,
                                       Rotation rotation,
                                       SubcircuitInstanceProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    CircuitSimulation simulation = circuitEditor.getCircuitSimulation();
    SubcircuitEditor instanceSubcircuitEditor = circuitEditor.getSubcircuitEditor(properties.subcircuitTypeName);
    return new SubcircuitInstanceView(subcircuitView,
                                      instanceSubcircuitEditor.getSubcircuitView(),
                                      simulation,
                                      position,
                                      rotation,
                                      properties);
  }

  @Override
  public Class<SubcircuitInstanceView> getViewClass()
  {
    return SubcircuitInstanceView.class;
  }

  @Override
  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, Logicim editor, StaticView<?> componentView)
  {
    return new SubcircuitInstancePropertyEditorDialog(parentFrame, editor, (SubcircuitInstanceView) componentView);
  }
}

