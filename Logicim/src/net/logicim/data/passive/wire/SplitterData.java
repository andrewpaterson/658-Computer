package net.logicim.data.passive.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;
import java.util.Set;

public class SplitterData
    extends PassiveData<SplitterView>
{
  protected int bitWidth;
  protected int fanOut;
  protected int girdSpacing;
  protected SplitterAppearance appearance;
  protected int endOffset;
  protected int[] splitIndices;

  public SplitterData()
  {
  }

  public SplitterData(Int2D position,
                      Rotation rotation,
                      String name,
                      Set<Long> simulationIDs,
                      List<SimulationMultiPortData> ports,
                      long id,
                      boolean enabled,
                      boolean selected,
                      int bitWidth,
                      int fanOut,
                      int girdSpacing,
                      SplitterAppearance appearance,
                      int endOffset,
                      int[] splitIndices)
  {
    super(position,
          rotation,
          name,
          simulationIDs,
          ports,
          id,
          enabled,
          selected);
    this.bitWidth = bitWidth;
    this.fanOut = fanOut;
    this.appearance = appearance;
    this.endOffset = endOffset;
    this.girdSpacing = girdSpacing;
    this.splitIndices = splitIndices;
  }

  @Override
  protected SplitterView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new SplitterView(subcircuitEditor.getCircuitSubcircuitView(),
                            position,
                            rotation,
                            new SplitterProperties(name,
                                                   bitWidth,
                                                   fanOut,
                                                   girdSpacing,
                                                   appearance,
                                                   endOffset,
                                                   splitIndices));
  }
}

