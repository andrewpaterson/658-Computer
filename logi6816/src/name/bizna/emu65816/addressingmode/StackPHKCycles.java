package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPHKCycles
    extends InstructionCycles
{
  //22c
  public StackPHKCycles(Consumer<Cpu65816> consumer)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), E(consumer), Write_DataLow(), SP_dec(), DONE()));
  }
}
