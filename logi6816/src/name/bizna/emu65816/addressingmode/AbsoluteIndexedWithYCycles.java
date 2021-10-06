package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithY;

public class AbsoluteIndexedWithYCycles
    extends InstructionCycles
{
  //7
  public AbsoluteIndexedWithYCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(AbsoluteIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), IO(), new NoteFourY(true)),
          new BusCycle(Address(DBR(), AA(), Y()), Read_DataLow(), E8Bit(operation), DONE8Bit(width)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(width)));
  }
}

