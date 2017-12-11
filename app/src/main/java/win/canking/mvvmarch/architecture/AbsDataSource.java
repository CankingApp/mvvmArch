package win.canking.mvvmarch.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

/**
 * Created by changxing on 2017/12/3.
 */

public abstract class AbsDataSource<ResultType, RequestType> {
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);


    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached getDate from the database
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<IRequestApi<RequestType>> createCall();


    @MainThread
    protected abstract void onFetchFailed();


    @MainThread
    public AbsDataSource() {
        final LiveData<ResultType> dbSource = loadFromDb();
        result.setValue(Resource.loading(dbSource.getValue()));

        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType resultType) {
                result.removeSource(dbSource);
                if (shouldFetch(resultType)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType resultType) {
                            result.setValue(Resource.success(resultType));
                        }
                    });
                }
            }
        });
    }


    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        final LiveData<IRequestApi<RequestType>> apiResponse = createCall();

        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType resultType) {
                result.setValue(Resource.loading(resultType));
            }
        });

        result.addSource(apiResponse, new Observer<IRequestApi<RequestType>>() {
            @Override
            public void onChanged(@Nullable final IRequestApi<RequestType> requestTypeRequestApi) {
                result.removeSource(apiResponse);
                result.removeSource(dbSource);
                //noinspection ConstantConditions
                if (requestTypeRequestApi.isSuccessful()) {
                    saveResultAndReInit(requestTypeRequestApi);
                } else {
                    onFetchFailed();

                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType resultType) {
                            result.setValue(
                                    Resource.error(requestTypeRequestApi.getErrorMsg(), resultType));
                        }
                    });
                }
            }
        });

    }

    @MainThread
    private void saveResultAndReInit(final IRequestApi<RequestType> response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response.getBody());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // we specially request a new live getDate,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.

                result.addSource(loadFromDb(), new Observer<ResultType>() {
                    @Override
                    public void onChanged(@Nullable ResultType resultType) {
                        result.setValue(Resource.success(resultType));
                    }
                });
            }
        }.execute();
    }

    public final MediatorLiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
