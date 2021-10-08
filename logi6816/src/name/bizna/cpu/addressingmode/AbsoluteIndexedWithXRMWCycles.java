package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.AbsoluteIndexedWithX;
import static name.bizna.cpu.WidthFromRegister.A;

public class AbsoluteIndexedWithXRMWCycles
    extends InstructionCycles
{
  //6b
  public AbsoluteIndexedWithXRMWCycles(Consumer<Cpu65816> operation)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(RMW), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), IO(RMW)),
          new BusCycle(Address(DBR(), AA(), X()), Read_DataLow(RMW)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Read_DataHigh(RMW), new NoteOne(A)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), IO(RMW), E(operation)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Write_DataHigh(RMW), new NoteOne(A)),
          new BusCycle(Address(DBR(), AA(), X()), Write_DataLow(RMW), DONE()));
  }
}

