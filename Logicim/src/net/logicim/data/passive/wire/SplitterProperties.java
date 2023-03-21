package net.logicim.data.passive.wire;

import net.logicim.common.SimulatorException;
import net.logicim.data.common.properties.ComponentProperties;

import java.util.Arrays;
import java.util.Objects;

public class SplitterProperties
    extends ComponentProperties
{
  public int bitWidth;
  public int fanOut;
  public int gridSpacing;
  public SplitterAppearance appearance;
  public int endOffset;
  public int[] splitIndices;

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
    SplitterProperties that = (SplitterProperties) o;
    return bitWidth == that.bitWidth &&
           fanOut == that.fanOut &&
           gridSpacing == that.gridSpacing &&
           endOffset == that.endOffset &&
           appearance == that.appearance &&
           Arrays.equals(splitIndices, that.splitIndices) &&
           Objects.equals(name, that.name);
  }

  @Override
  public SplitterProperties duplicate()
  {
    return new SplitterProperties(name,
                                  bitWidth,
                                  fanOut,
                                  gridSpacing,
                                  appearance,
                                  endOffset,
                                  Arrays.copyOf(splitIndices, splitIndices.length));
  }
}

