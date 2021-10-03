package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPullCycles
    extends InstructionCycles
{
  //22b
  public StackPullCycles(Consumer<Cpu65816> consumer, Width width)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc(), E8Bit(consumer), DONE8Bit(width)),
          new BusCycle(Address(S(), o(1)), Read_DataHigh(), SP_inc(), E16Bit(consumer), DONE16Bit(width)));
  }
}

