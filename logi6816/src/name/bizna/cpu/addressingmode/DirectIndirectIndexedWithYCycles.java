package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.DirectIndirectIndexedWithY;

public class DirectIndirectIndexedWithYCycles
    extends InstructionCycles
{
  //13
  public DirectIndirectIndexedWithYCycles(Consumer<Cpu65816> operation)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), IO(), new NoteFourY(true)),
          new BusCycle(Address(DBR(), AA(), Y()), Read_DataLow(), E8Bit(operation), DONE8Bit(Width.A)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(Width.A)));
  }
}

