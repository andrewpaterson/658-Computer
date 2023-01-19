package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.common.SimulatorException;
import net.logicim.data.passive.wire.SplitterAppearance;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;

import java.util.Arrays;

public class SplitterProperties
    extends ComponentProperties
{
  protected int bitWidth;
  protected int fanOut;
  protected int gridSpacing;
  protected SplitterAppearance appearance;
  protected int endOffset;
  protected int[] splitIndices;

  public SplitterProperties()
  {
  }

  public SplitterProperties(String name,
                            int bitWidth,
                            int fanOut,
                            int gridSpacing,
                            SplitterAppearance appearance,
                            int endOffset,
                            int[] splitIndices)
  {
    super(name);
    this.bitWidth = bitWidth;
    this.fanOut = fanOut;
    this.appearance = appearance;
    this.endOffset = endOffset;
    this.gridSpacing = gridSpacing;
    this.splitIndices = splitIndices;

    if (splitIndices.length != bitWidth)
    {
      throw new SimulatorException("Split indices size must match bit-width");
    }
  }

  public SplitterProperties(String name,
                            int bitWidth,
                            int fanOut,
                            int gridSpacing,
                            SplitterAppearance appearance,
                            int endOffset)
  {
    this(name,
         bitWidth,
         fanOut,
         gridSpacing,
         appearance,
         endOffset,
         createSplitIndices(bitWidth, fanOut));
  }

  public static int[] createSplitIndices(int bitWidth, int endCount)
  {
    int[] splitIndices;
    if (endCount == 1)
    {
      splitIndices = new int[bitWidth];
      Arrays.fill(splitIndices, 0);
    }
    else
    {
      splitIndices = fillEndIndices(endCount, bitWidth);
    }
    return splitIndices;
  }

  private static int[] fillEndIndices(int endCount, int totalWidth)
  {
    int sourceCounter = 0;
    int destCounter;
    int d = endCount / 2;

    int[] temp = new int[totalWidth];
    for (destCounter = 0; destCounter < totalWidth; destCounter++)
    {
      temp[destCounter] = sourceCounter;
      d = d + endCount;
      if (d >= totalWidth)
      {
        d = d - totalWidth;
        sourceCounter++;
      }
    }
    return temp;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    SplitterProperties other = (SplitterProperties) o;
    return bitWidth == other.bitWidth &&
           fanOut == other.fanOut &&
           endOffset == other.endOffset &&
           gridSpacing == other.gridSpacing &&
           Arrays.equals(splitIndices, other.splitIndices);
  }

  @Override
  public int hashCode()
  {
    return 0;
  }
}

