package net.logicim.ui.circuit;

import net.common.type.Int2D;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.path.UpdatedViewPaths;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;
import java.util.Set;

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
    SubcircuitInstanceProperties defaultProperties = createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true);
    defaultProperties.subcircuitTypeName = subcircuitTypeName;
    return create(circuitEditor,
                  circuitEditor.getCurrentSubcircuitView(),
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
                                       SubcircuitView containingSubcircuitView,
                                       Int2D position,
                                       Rotation rotation,
                                       SubcircuitInstanceProperties properties)
  {
    SubcircuitEditor instanceSubcircuitEditor = circuitEditor.getSubcircuitEditor(properties.subcircuitTypeName);
    SubcircuitView instanceSubcircuitView = instanceSubcircuitEditor.getInstanceSubcircuitView();
    SubcircuitInstanceView subcircuitInstanceView = new SubcircuitInstanceView(containingSubcircuitView,
                                                                               instanceSubcircuitView,
                                                                               position,
                                                                               rotation,
                                                                               properties);
    UpdatedViewPaths updatedPaths = circuitEditor.viewPathsUpdate();

    Set<SubcircuitView> updatedSubcircuitViews = updatedPaths.getSubcircuitViews();
    for (SubcircuitView subcircuitView : updatedSubcircuitViews)
    {
      subcircuitView.pathsUpdated(updatedPaths);
    }
    return subcircuitInstanceView;
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

