package name.bizna.emu65816;

public class Main
{
  public static void main(String[] args)
  {
    EmulationModeInterrupts emi = new EmulationModeInterrupts((short) 0xfff4,
                                                              (short) 0xfff6,
                                                              (short) 0xfff8,
                                                              (short) 0xfffa,
                                                              (short) 0xfffc,
                                                              (short) 0xfffe);
    NativeModeInterrupts nmi = new NativeModeInterrupts((short) 0xffe4,
                                                        (short) 0xffe6,
                                                        (short) 0xffe8,
                                                        (short) 0xffea,
                                                        (short) 0xfffc,
                                                        (short) 0xffee);
    SystemBus systemBus = new SystemBus();

    MemoryDevice memory = new MemoryDevice(128 * MemoryDevice.KB);

    memory.set(0xFFFC, "0006");
    memory.set(0x0600, "200606203806200D06202A0660A90285");
    memory.set(0x0610, "02A9048503A9118510A9108512A90F85");
    memory.set(0x0620, "14A90485118513851560A5FE8500A5FE");
    memory.set(0x0630, "2903186902850160204D06208D0620C3");
    memory.set(0x0640, "06201907202007202D074C3806A5FFC9");
    memory.set(0x0650, "77F00DC964F014C973F01BC961F02260");
    memory.set(0x0660, "A9042402D026A901850260A9082402D0");
    memory.set(0x0670, "1BA902850260A9012402D010A9048502");
    memory.set(0x0680, "60A9022402D005A90885026060209406");
    memory.set(0x0690, "20A80660A500C510D00DA501C511D007");
    memory.set(0x06A0, "E603E603202A0660A202B510C510D006");
    memory.set(0x06B0, "B511C511F009E8E8E403F0064CAA064C");
    memory.set(0x06C0, "350760A603CA8AB5109512CA10F9A502");
    memory.set(0x06D0, "4AB0094AB0194AB01F4AB02FA51038E9");
    memory.set(0x06E0, "208510900160C611A901C511F02860E6");
    memory.set(0x06F0, "10A91F2410F01F60A5101869208510B0");
    memory.set(0x0700, "0160E611A906C511F00C60C610A51029");
    memory.set(0x0710, "1FC91FF001604C3507A000A5FE910060");
    memory.set(0x0720, "A200A9018110A603A900811060A200EA");
    memory.set(0x0730, "EACAD0FB60");

    systemBus.registerDevice(memory);

    Cpu65816 cpu = new Cpu65816(systemBus, emi, nmi);
    cpu.setRESPin(false);

    for (; ; )
    {
      cpu.executeNextInstruction();
    }
  }
}

