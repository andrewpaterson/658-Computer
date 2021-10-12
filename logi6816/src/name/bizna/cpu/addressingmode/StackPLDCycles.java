package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Stack;

public class StackPLDCycles
    extends InstructionCycles
{
  //22b
  public StackPLDCycles(Executor<Cpu65816> consumer)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc()),
          new BusCycle(Address(S(), o(1)), Read_DataHigh(), SP_inc(), E(consumer), DONE()));
  }
}

