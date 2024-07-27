package net.logicim.ui.input;

import net.common.reflect.PackageInspector;
import net.common.reflect.PackageInspectorStore;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.editor.EditPropertiesAction;
import net.logicim.ui.editor.EditorAction;
import net.logicim.ui.editor.EnterSubcircuitAction;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.lang.reflect.Modifier;
import java.util.List;

public class ComponentDoubleClickInputsFactory
{
  public static void create(Logicim editor)
  {
    EditorAction editPropertiesAction = editor.getAction(EditPropertiesAction.NAME);

    PackageInspector packageInspector = PackageInspectorStore.getInstance().getPackageInspector("net.logicim.ui");
    List<Class<ComponentView<?>>> subClasses = (List) packageInspector.getSubClasses(ComponentView.class);
    for (Class<ComponentView<?>> subClass : subClasses)
    {
      if (!(Modifier.isAbstract(subClass.getModifiers()) || subClass.isInterface()))
      {
        editor.addComponentDoubleClickedInput(subClass, editPropertiesAction);
      }
    }
    EditorAction enterSubcircuitAction = editor.getAction(EnterSubcircuitAction.NAME);
    editor.addComponentDoubleClickedInput(SubcircuitInstanceView.class, enterSubcircuitAction);
  }
}

