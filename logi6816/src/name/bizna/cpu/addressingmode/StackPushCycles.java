package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.AddressingMode.Stack;

public class StackPushCycles
    extends InstructionCycles
{
  //22c
  public StackPushCycles(Executor<Cpu65816> consumer, WidthFromRegister width)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), E(consumer)),
          new BusCycle(Address(S()), Write_DataHigh(), SP_dec(), new NoteOne(width)),
          new BusCycle(Address(S()), Write_DataLow(), SP_dec(), DONE()));
  }
}

