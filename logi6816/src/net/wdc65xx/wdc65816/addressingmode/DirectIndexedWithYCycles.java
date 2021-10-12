package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.WidthFromRegister;

import static net.wdc65xx.wdc65816.AddressingMode.DirectIndexedWithY;

public class DirectIndexedWithYCycles
    extends InstructionCycles
{
  //17
  public DirectIndexedWithYCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(DirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), Y()), Read_DataLow(), E8Bit(operation, width), DONE8Bit(width)),
          new BusCycle(Address(DP(), D0(), Y(), o(1)), Read_DataHigh(), E16Bit(operation, width), DONE16Bit(width)));
  }
}

