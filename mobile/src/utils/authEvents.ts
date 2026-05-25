// axios 인터셉터에서 토큰 갱신 실패 시 AuthContext의 logout을 호출하기 위한 브릿지
type Callback = () => void;
let onLogout: Callback | null = null;

export const authEvents = {
  setLogoutHandler: (cb: Callback) => { onLogout = cb; },
  triggerLogout: () => { onLogout?.(); },
};
