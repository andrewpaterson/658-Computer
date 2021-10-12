package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.AddressingMode.Stack;

public class StackPullCycles
    extends InstructionCycles
{
  //22b
  public StackPullCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc(), E8Bit(operation, width), DONE8Bit(width)),
          new BusCycle(Address(S(), o(1)), Read_DataHigh(), SP_inc(), E16Bit(operation, width), DONE16Bit(width)));
  }
}

