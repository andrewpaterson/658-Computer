package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.DirectIndirect;

public class DirectIndirectCycles
    extends InstructionCycles
{
  public DirectIndirectCycles(Consumer<Cpu65816> operation)
  {
    //12
    super(DirectIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AA()), Read_DataLow(), E8Bit(operation), DONE8Bit(Width.A)),
          new BusCycle(Address(DBR(), AA(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(Width.A)));
  }
}

