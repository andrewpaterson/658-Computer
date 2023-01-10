package net.logicim.data.passive.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.passive.splitter.SplitterProperties;
import net.logicim.ui.integratedcircuit.standard.passive.splitter.SplitterView;

import java.util.List;

public class SplitterData
    extends PassiveData<SplitterView>
{
  protected int bitWidth;
  protected int endCount;
  protected int endOffset;
  protected int girdSpacing;
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
                      int endCount,
                      int endOffset,
                      int girdSpacing,
                      int[] splitIndices)
  {
    super(position, rotation, name, ports, selected);
    this.bitWidth = bitWidth;
    this.endCount = endCount;
    this.endOffset = endOffset;
    this.girdSpacing = girdSpacing;
    this.splitIndices = splitIndices;
  }

  @Override
  protected SplitterView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new SplitterView(circuitEditor,
                            position,
                            rotation,
                            new SplitterProperties(name,
                                                   bitWidth,
                                                   endCount,
                                                   endOffset,
                                                   girdSpacing,
                                                   splitIndices));
  }
}

