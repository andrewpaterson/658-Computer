package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Stack;

public class StackPHKCycles
    extends InstructionCycles
{
  //22c
  public StackPHKCycles(Executor<Cpu65816> consumer)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), E(consumer), Write_DataLow(), SP_dec(), DONE()));
  }
}

