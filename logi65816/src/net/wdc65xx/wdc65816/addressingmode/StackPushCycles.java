package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.WidthFromRegister;

import static net.wdc65xx.wdc65816.AddressingMode.Stack;

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

