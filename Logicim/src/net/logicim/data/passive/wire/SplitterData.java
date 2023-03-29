package net.logicim.data.passive.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.List;

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
                      List<MultiPortData> ports,
                      boolean selected,
                      int bitWidth,
                      int fanOut,
                      int girdSpacing,
                      SplitterAppearance appearance,
                      int endOffset,
                      int[] splitIndices)
  {
    super(position, rotation, name, ports, selected);
    this.bitWidth = bitWidth;
    this.fanOut = fanOut;
    this.appearance = appearance;
    this.endOffset = endOffset;
    this.girdSpacing = girdSpacing;
    this.splitIndices = splitIndices;
  }

  @Override
  protected SplitterView create(SubcircuitEditor subcircuitEditor, Circuit circuit, TraceLoader traceLoader, boolean fullLoad)
  {
    return new SplitterView(subcircuitEditor.getSubcircuitView(),
                            circuit,
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

