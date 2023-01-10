package net.logicim.ui.integratedcircuit.standard.passive.splitter;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

import java.util.Arrays;

public class SplitterProperties
    extends ComponentProperties
{
  protected int bitWidth;
  protected int endCount;
  protected int endOffset;
  protected int gridSpacing;
  protected int[] splitIndices;

  public SplitterProperties()
  {
  }

  public SplitterProperties(String name,
                            int bitWidth,
                            int endCount,
                            int endOffset,
                            int gridSpacing,
                            int[] splitIndices)
  {
    super(name);
    this.bitWidth = bitWidth;
    this.endCount = endCount;
    this.endOffset = endOffset;
    this.gridSpacing = gridSpacing;
    this.splitIndices = splitIndices;
  }

  public SplitterProperties(String name,
                            int bitWidth,
                            int endCount,
                            int endOffset,
                            int gridSpacing)
  {
    this(name,
         bitWidth,
         endCount,
         endOffset,
         gridSpacing,
         createSplitIndices(bitWidth, endCount));
  }

  private static int[] createSplitIndices(int bitWidth, int endCount)
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

//    for (int i = 0; i < temp.length; i++)
//    {
//      int distributionIndex = temp[i];
//      System.out.print(distributionIndex + ", ");
//      temp[i] = distributionIndex;
//    }
    return temp;
  }
}