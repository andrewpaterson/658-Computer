package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.AbsoluteIndexedWithY;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class AbsoluteIndexedWithYWriteCycles
    extends InstructionCycles
{
  //7
  public AbsoluteIndexedWithYWriteCycles(Executor<Cpu65816> operation)
  {
    super(AbsoluteIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), IO(), new NoteFourY(false)),
          new BusCycle(Address(DBR(), AA(), Y()), E(operation), Write_DataLow(), DONE8Bit(M)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Write_DataHigh(), DONE16Bit(M)));
  }
}

