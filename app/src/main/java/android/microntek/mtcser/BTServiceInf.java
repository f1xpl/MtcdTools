package android.microntek.mtcser;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface BTServiceInf extends IInterface {

    public static abstract class Stub extends Binder implements BTServiceInf {

        private static class Proxy implements BTServiceInf {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void init() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte getBTState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte getAVState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDialOutNum() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCallInNum() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPhoneNum() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getNowDevAddr() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getNowDevName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void avPlayPause() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void avPlayStop() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void avPlayPrev() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void avPlayNext() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void answerCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hangupCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void rejectCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchVoice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void syncPhonebook() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getModuleName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getModulePassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setModuleName(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(name);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setModulePassword(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(password);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAutoConnect(boolean auto) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    if (auto) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getAutoConnect() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void setAutoAnswer(boolean auto) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    if (auto) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getAutoAnswer() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void connectBT(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(mac);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnectBT(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(mac);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectOBD(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(mac);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnectOBD(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(mac);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteOBD(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(mac);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteBT(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(mac);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void syncMatchList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getMatchList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getDeviceList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getHistoryList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getPhoneBookList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPhoneBookList(List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeStringList(list);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteHistory(int idx) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeInt(idx);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteHistoryAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void musicMute() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void musicUnmute() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void scanStart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void scanStop() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dialOut(String s) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeString(s);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dialOutSub(char b) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    _data.writeInt(b);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reDial() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMusicInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getOBDstate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.microntek.mtcser.BTServiceInf");
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static BTServiceInf asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("android.microntek.mtcser.BTServiceInf");
            if (iin == null || !(iin instanceof BTServiceInf)) {
                return new Proxy(obj);
            }
            return (BTServiceInf) iin;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _arg0 = 0;
            byte _result;
            String _result2;
            boolean _arg02 = false;
            boolean _result3;
            List<String> _result4;
            switch (code) {
                case 1:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    init();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result = getBTState();
                    reply.writeNoException();
                    reply.writeByte(_result);
                    return true;
                case 3:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result = getAVState();
                    reply.writeNoException();
                    reply.writeByte(_result);
                    return true;
                case 4:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result2 = getDialOutNum();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 5:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result2 = getCallInNum();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 6:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result2 = getPhoneNum();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 7:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    long _result5 = getNowDevAddr();
                    reply.writeNoException();
                    reply.writeLong(_result5);
                    return true;
                case 8:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result2 = getNowDevName();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 9:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    avPlayPause();
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    avPlayStop();
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    avPlayPrev();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    avPlayNext();
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    answerCall();
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    hangupCall();
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    rejectCall();
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    switchVoice();
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    syncPhonebook();
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result2 = getModuleName();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 19:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result2 = getModulePassword();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 20:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    setModuleName(data.readString());
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    setModulePassword(data.readString());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    }
                    setAutoConnect(_arg02);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result3 = getAutoConnect();
                    reply.writeNoException();
                    if (_result3) {
                        _arg0 = 1;
                    }
                    reply.writeInt(_arg0);
                    return true;
                case 24:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    }
                    setAutoAnswer(_arg02);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result3 = getAutoAnswer();
                    reply.writeNoException();
                    if (_result3) {
                        _arg0 = 1;
                    }
                    reply.writeInt(_arg0);
                    return true;
                case 26:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    connectBT(data.readString());
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    disconnectBT(data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    connectOBD(data.readString());
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    disconnectOBD(data.readString());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    deleteOBD(data.readString());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    deleteBT(data.readString());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    syncMatchList();
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result4 = getMatchList();
                    reply.writeNoException();
                    reply.writeStringList(_result4);
                    return true;
                case 34:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result4 = getDeviceList();
                    reply.writeNoException();
                    reply.writeStringList(_result4);
                    return true;
                case 35:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result4 = getHistoryList();
                    reply.writeNoException();
                    reply.writeStringList(_result4);
                    return true;
                case 36:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result4 = getPhoneBookList();
                    reply.writeNoException();
                    reply.writeStringList(_result4);
                    return true;
                case 37:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    setPhoneBookList(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    deleteHistory(data.readInt());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    deleteHistoryAll();
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    musicMute();
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    musicUnmute();
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    scanStart();
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    scanStop();
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    dialOut(data.readString());
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    dialOutSub((char) data.readInt());
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    reDial();
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    _result2 = getMusicInfo();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 48:
                    data.enforceInterface("android.microntek.mtcser.BTServiceInf");
                    int _result6 = getOBDstate();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 1598968902:
                    reply.writeString("android.microntek.mtcser.BTServiceInf");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void answerCall() throws RemoteException;

    void avPlayNext() throws RemoteException;

    void avPlayPause() throws RemoteException;

    void avPlayPrev() throws RemoteException;

    void avPlayStop() throws RemoteException;

    void connectBT(String str) throws RemoteException;

    void connectOBD(String str) throws RemoteException;

    void deleteBT(String str) throws RemoteException;

    void deleteHistory(int i) throws RemoteException;

    void deleteHistoryAll() throws RemoteException;

    void deleteOBD(String str) throws RemoteException;

    void dialOut(String str) throws RemoteException;

    void dialOutSub(char c) throws RemoteException;

    void disconnectBT(String str) throws RemoteException;

    void disconnectOBD(String str) throws RemoteException;

    byte getAVState() throws RemoteException;

    boolean getAutoAnswer() throws RemoteException;

    boolean getAutoConnect() throws RemoteException;

    byte getBTState() throws RemoteException;

    String getCallInNum() throws RemoteException;

    List<String> getDeviceList() throws RemoteException;

    String getDialOutNum() throws RemoteException;

    List<String> getHistoryList() throws RemoteException;

    List<String> getMatchList() throws RemoteException;

    String getModuleName() throws RemoteException;

    String getModulePassword() throws RemoteException;

    String getMusicInfo() throws RemoteException;

    long getNowDevAddr() throws RemoteException;

    String getNowDevName() throws RemoteException;

    int getOBDstate() throws RemoteException;

    List<String> getPhoneBookList() throws RemoteException;

    String getPhoneNum() throws RemoteException;

    void hangupCall() throws RemoteException;

    void init() throws RemoteException;

    void musicMute() throws RemoteException;

    void musicUnmute() throws RemoteException;

    void reDial() throws RemoteException;

    void rejectCall() throws RemoteException;

    void scanStart() throws RemoteException;

    void scanStop() throws RemoteException;

    void setAutoAnswer(boolean z) throws RemoteException;

    void setAutoConnect(boolean z) throws RemoteException;

    void setModuleName(String str) throws RemoteException;

    void setModulePassword(String str) throws RemoteException;

    void setPhoneBookList(List<String> list) throws RemoteException;

    void switchVoice() throws RemoteException;

    void syncMatchList() throws RemoteException;

    void syncPhonebook() throws RemoteException;
}
