import { Disbursement } from '../types';

export type AuthStackParamList = {
  Login: undefined;
};

export type HomeStackParamList = {
  Home: undefined;
  DisbursementForm: { disbursement?: Disbursement };
};

export type RootTabParamList = {
  HomeTab: undefined;
  StatsTab: undefined;
};
