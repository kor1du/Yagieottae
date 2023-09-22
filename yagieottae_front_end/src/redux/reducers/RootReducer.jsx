import { combineReducers } from "redux";
import ServerURLReducer from "redux/reducers/ServerURLReducer";
import UserReducer from 'redux/reducers/UserReducer';
import ModalReducer from 'redux/reducers/ModalReducer';
import storage from 'redux-persist/lib/storage';
import persistReducer from "redux-persist/es/persistReducer";

const persistConfig = {
  key: "root",
  storage: storage,
  whitelist: ["UserReducer"]
}

const rootReducer = combineReducers({
  ServerURLReducer, UserReducer, ModalReducer
});

export default persistReducer(persistConfig, rootReducer);
