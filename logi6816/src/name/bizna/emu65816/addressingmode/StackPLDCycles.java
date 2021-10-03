package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPLDCycles
    extends InstructionCycles
{
  //22b
  public StackPLDCycles(Consumer<Cpu65816> consumer)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc()),
          new BusCycle(Address(S(), o(1)), Read_DataHigh(), SP_inc(), E(consumer), DONE()));
  }
}

