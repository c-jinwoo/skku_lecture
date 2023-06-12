"""Skeleton Code for PA1"""

# -*- coding: utf-8 -*-

#   *** Do not import any library except already imported libraries ***
from util import *
from mlp import MLP
#   *** Do not import any library except already imported libraries ***

class Preprocessing(AI_util):
    #   *** Do not modify the code below ***
    def Calculate_TF_IDF_Normalization(self, data: List[Tuple[str, List[str], int]], data_type: str = 'train')  -> List[Tuple[str, List[float], int]]:
        total_TF = list()
        normalized_tf_idf = list()
        document_freq = {i: 0 for i in range(len(self.word2idx))}
    #   *** Do not modify the code above ***

        # 1. **Train data** Document Frequency (df) Calculate And Sort with df value 
        ##### EDIT HERE #####

        if data_type == "train":
            for datum in data:
                words = set(datum[1])                                                   # words = 단어 리스트
                for word in words:
                    if word in self.word2idx:                   
                        document_freq[self.word2idx[word]] += 1                         # word2idx에 단어가 있다면 df의 key 인덱스의 value 증가
                        
            sorted_df = sorted(document_freq.items(), key=lambda x: x[1])               # df의 key를 기준으로 정렬한 Dictionary 저장
            
        ##### END #####
       
        # 2. **Train data** Erase self.word2idx Dictionary value not in top 15000
        ##### EDIT HERE #####

        if data_type == "train":
            top_df = [x[0] for x in sorted_df[:15000]]                                  # 정렬된 df 중 상위 15000개 추출  
            top_word2idx = {k: v for k, v in self.word2idx.items() if v in top_df}      # word2idx의 value를 기준으로 top_df의 순서에 맞게 저장
            self.word2idx = top_word2idx                                                # 최종 결과 Dictionary를 self.word2idx에 저장

        ##### END #####

        # 3. Term Frequency (tf) And Document Frequency (df) Calculate
        #   *** Do not modify the code below ***
        
        document_freq = {i: 0 for i in self.word2idx.values()}
        
        for _, paragraph, _ in tqdm(data, desc='tf calculate'):
            term_freq = list()
            for token, idx in self.word2idx.items():
                tf = paragraph.count(token)
                term_freq.append(tf)
                if tf != 0:
                    document_freq[idx] += 1
            total_TF.append(term_freq)
        #   *** Do not modify the code above ***

        # 4. **Train data** Inverse Document Frequencey (IDF) Calculate
        ##### EDIT HERE #####

        if data_type == "train":
            self.idf = list()                                                           # IDF 리스트(test에도 쓸 것이므로 클래스 변수로 저장)
            data_cnt = len(data)                                                        # 전체 데이터 수
            for token, idx in self.word2idx.items():                                        
                self.idf.append(math.log(data_cnt / document_freq[idx], 2))             # IDF 계산(log2(전체데이터/DF))하여 추가

        ##### END #####
        
        # 5. Normalized TF-IDF Calculate
        ##### EDIT HERE #####

        for i in range(len(total_TF)):
            result = list()
            
            for j in range(len(total_TF[i])):
                result.append(total_TF[i][j] * self.idf[j])                             # TF * IDF 리스트(정규화 전)
                
            np_result = np.array(result)
            norm = np.linalg.norm(np_result)                                            # 정규화 분모 계산
            
            if norm == 0:
                tfidf_norm = np.zeros_like(np_result)                                   # 분모가 0이면 값을 0으로
            else:
                tfidf_norm = np.round(np_result / norm, 2)                              # 분모가 0이 아니면 나누고 소수 둘째자리까지
            normalized_tf_idf.append(((data[i][0], tfidf_norm.tolist(), data[i][2])))   # 리턴형(tuple(str, list, int))으로 저장

        ##### END #####
        
        
        return normalized_tf_idf[:]


#   *** Do not modify the code below ***
class TrainModels():
    def __init__(self, data, label2idx):
        train, test = data
        self.label2idx = label2idx
        self.train_input = list()
        self.train_label = list()
        for data in train:
            self.train_input.append(data[-2])
            self.train_label.append(data[-1])
        self.test_input = list()
        self.test_label = list()
        for data in test:
            self.test_input.append(data[-2])
            self.test_label.append(data[-1])   

    def calculate_score(self, preds):
        label_correct = {0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0}
        correct = 0

        for label, pred in zip(self.test_label, preds):
            if pred == label:
                correct += 1
                label_correct[label] += 1

        tmp_result = {
            'accuracy': ((correct / len(self.test_label)) * 100, len(self.test_label)),
        }
        for k, v in self.label2idx.items():
            precision = (label_correct[v] / preds.count(v)) if preds.count(v) != 0 else 0.0
            recall = (label_correct[v] / self.test_label.count(v)) if self.test_label.count(v) != 0 else 0.0
            f1 = (2 * (precision * recall) / (recall + precision)) if recall + precision != 0 else 0.0
            tmp_result[k] = (precision * 100, recall * 100, f1 * 100, self.test_label.count(v))

        micro_avg_pre = (sum(list(label_correct.values())) / len(self.test_label))
        micro_avg_rec = (sum(list(label_correct.values())) / len(self.test_label))
        micro_avg_f1 = 2 * (micro_avg_pre * micro_avg_rec) / (micro_avg_rec + micro_avg_pre)
        tmp_result['micro avg'] = (micro_avg_pre * 100, micro_avg_rec * 100, micro_avg_f1 * 100, len(self.test_label))

        return tmp_result

    def get_euclidean_dist(self, vec_a, vec_b):
        dist = math.sqrt(sum(pow(a-b, 2) for a, b in zip(vec_a, vec_b)))

        return dist

    def knn_classifier(self):
        """
            This function is for evaluating (testing) KNN Model.
            (Inference may take some time…)
        """
        print("\nPredicting with KNN…")
        K = 10
        knn_euclidean_preds = []
        for test_vec in self.test_input:
            knn_predict = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            test_dist = []
            sort_dist = []
            for train_vec in self.train_input:
                test_dist.append(self.get_euclidean_dist(test_vec, train_vec))
            sort_dist = sorted(test_dist)
            sort_dist = sort_dist[:K]
            for dist in sort_dist:
                knn_predict[self.train_label[test_dist.index(dist)]] += 1
            knn_euclidean_preds.append(knn_predict.index(max(knn_predict)))
        knn_result = self.calculate_score(knn_euclidean_preds)
        print("Evaluating with KNN END!")
        
        return knn_result

    def svm_classifier(self):
        """
            This function is for training and evaluating (testing) SVM Model.
        """
        print("\nPredicting with SVM...")
        classifier = LinearSVC(C=1.0, max_iter=2000)
        classifier.fit(self.train_input, self.train_label)

        svm_preds = classifier.predict(self.test_input).tolist()
        svm_result = self.calculate_score(svm_preds)
        print("Training and evaluating with SVM END!")

        return svm_result
    
    def mlp_classifier(self):
        """
            This function is for training and evaluating (testing) MLP Model.
            (Training may take some time...)
        """
        print("\nPredicting with MLP...")
        mlp = MLP(
            input_size=len(self.train_input[0]), 
            hidden_size=200,
            output_size=len(self.label2idx),
            learning_rate=0.05
        )

        d = list(zip(self.train_input, self.train_label))
        for epoch in trange(20, desc='Epoch...'):
            epoch_loss = .0
            random.shuffle(d)
            for train_vec, label in tqdm(d, desc='Train iter'):
                logits = mlp.forward(train_vec)
                loss = mlp.loss(label, logits)
                mlp.backward()
                mlp.step()

                epoch_loss += loss

            print('Epoch : {} | Loss : {:.4f}'.format(epoch+1, epoch_loss / len(d)))

        mlp_preds = list()
        for test_vec, label in zip(self.test_input, self.test_label):
            logits = mlp.forward(test_vec)
            max_label = np.argmax(logits)
            mlp_preds.append(max_label)
        mlp_result = self.calculate_score(mlp_preds)
        print("Training and evaluating with MLP END!")

        return mlp_result
    #   *** Do not modify the code above ***


def main(data, label2idx, std_name, std_id):
    # *** Do not modify the code below ***
    result = dict()

    result['knn'] = None
    result['svm'] = None
    result['mlp'] = None
    #   *** Do not modify the code above ***

    """
    1. Train the machine learning models (e.g., KNN, SVM, MLP) using the normalized TF-IDF vectors
        - Training and evaluation code is already implemented
        - Use the appropriate fuctions to train the models

    2. Store the evaluation results
    """
    ##### EDIT HERE #####

    model = TrainModels(data, label2idx)
    result['knn'] = model.knn_classifier()
    result['svm'] = model.svm_classifier()
    result['mlp'] = model.mlp_classifier()

    ##### END #####

    save_test_result(result, std_name=std_name, std_id=std_id)



if __name__ == "__main__":
    #   *** Do not modify the code below ***
    random.seed(42)
    np.random.seed(42)

    parser = argparse.ArgumentParser()

    parser.add_argument("--paragraph_id",
                        default=888,
                        type=int)
    args = parser.parse_args()
    #   *** Do not modify the code above ***


    ### IMPORTANT ###
    ### Please write your own name and student ID.
    NAME = "ChoiJinWoo"
    ID = "2020315798"
    

    #   *** Do not modify the code below ***
    Preprocessing = Preprocessing()
    train_data = Preprocessing.load_data(data_path='Data/train.json', data_type='train')
    Preprocessing.train_tfidf = Preprocessing.Calculate_TF_IDF_Normalization(data=train_data)
    test_data = Preprocessing.load_data(data_path='Data/test.json', data_type='test')
    Preprocessing.test_tfidf = Preprocessing.Calculate_TF_IDF_Normalization(data=test_data, data_type='test')

    Preprocessing.save_preprocess_result(std_name=NAME, std_id =ID, paragraph_id=args.paragraph_id)

    data = (Preprocessing.train_tfidf, Preprocessing.test_tfidf)
    main(data, Preprocessing.label2idx, std_name=NAME, std_id =ID)
    #   *** Do not modify the code above ***
